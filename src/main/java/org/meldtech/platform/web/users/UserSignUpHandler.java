package org.meldtech.platform.web.users;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.meldtech.platform.config.AuthTokenConfig;
import org.meldtech.platform.config.CustomValidator;
import org.meldtech.platform.model.api.ApiResponse;
import org.meldtech.platform.model.api.request.PasswordRestRecord;
import org.meldtech.platform.model.api.request.UserRecord;
import org.meldtech.platform.model.constant.VerificationType;
import org.meldtech.platform.model.dto.UploadFileRequest;
import org.meldtech.platform.service.UserSignUpService;
import org.meldtech.platform.service.cloudinary.MeldFileProcessor;
import org.meldtech.platform.util.LoggerHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.meldtech.platform.model.api.ApiResponse.buildServerResponse;

@Service
@RequiredArgsConstructor
public class UserSignUpHandler {
    private final LoggerHelper log = LoggerHelper.newInstance(UserSignUpHandler.class.getName());
    private final CustomValidator customValidator;
    private final UserSignUpService userSignUpService;
    private final MeldFileProcessor meldFileProcessor;

    @Value("${mail.reset.password.template}")
    private String resetPasswordTemplate;
    @Value("${mail.otp.template}")
    private String otpTemplate;

    private static final String X_FORWARD_FOR = "X-Forwarded-For";

    public Mono<ServerResponse> addNewUser(ServerRequest request)  {
        Mono<UserRecord> profileRecordMono = request.bodyToMono(UserRecord.class)
                .doOnNext(customValidator::validateEntries);
        log.info("Create new user Requested", request.headers().firstHeader(X_FORWARD_FOR));
        return profileRecordMono
                .map(userSignUpService::createUser)
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> addNewUserPublic(ServerRequest request)  {
        Mono<UserRecord> profileRecordMono = request.bodyToMono(UserRecord.class)
                .doOnNext(customValidator::validateEntries);
        log.info("Signing up new user Requested", request.headers().firstHeader(X_FORWARD_FOR));
        return profileRecordMono
                .map(userSignUpService::createUser)
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> resetUserPassword(ServerRequest request)  {
        String email = request.pathVariable("email");
        log.info("Reset user password (public) Requested", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(userSignUpService.resetPasswordRequest(email, resetPasswordTemplate));
    }

    public Mono<ServerResponse> changePasswordPublic(ServerRequest request)  {
        Mono<PasswordRestRecord> passwordRecordMono = request.bodyToMono(PasswordRestRecord.class)
                .doOnNext(customValidator::validateEntries);
        String otp = request.pathVariable("otp");
        log.info("Change user password public Requested ", request.headers().firstHeader(X_FORWARD_FOR));
        return passwordRecordMono.map(passwordRestRecord ->
                        userSignUpService.verifyPasswordResetOtp(otp, passwordRestRecord))
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> changePasswordPublicById(ServerRequest request)  {
        Mono<PasswordRestRecord> passwordRecordMono = request.bodyToMono(PasswordRestRecord.class)
                .doOnNext(customValidator::validateEntries);
        String publicId = request.pathVariable("publicId");
        log.info("User change user password Requested", request.headers().firstHeader(X_FORWARD_FOR));
        return  passwordRecordMono.map(passwordRestRecord ->
                        userSignUpService.changePassword(publicId, passwordRestRecord))
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> changePassword(ServerRequest request)  {
        Mono<PasswordRestRecord> passwordRecordMono = request.bodyToMono(PasswordRestRecord.class)
                .doOnNext(customValidator::validateEntries);
        Mono<JwtAuthenticationToken> jwtAuthToken = AuthTokenConfig.authenticatedToken(request);
        log.info("Change user password Requested", request.headers().firstHeader(X_FORWARD_FOR));
        return jwtAuthToken
                .map(ApiResponse::getPublicIdFromToken)
                        .flatMap(id -> passwordRecordMono
                                .map(passwordRestRecord ->
                                        userSignUpService.changePassword(id, passwordRestRecord)))
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> changePasswordByAdmin(ServerRequest request)  {
        Mono<PasswordRestRecord> passwordRecordMono = request.bodyToMono(PasswordRestRecord.class)
                .doOnNext(customValidator::validateEntries);
        String publicId = request.pathVariable("publicId");
        log.info("Admin change user password Requested", request.headers().firstHeader(X_FORWARD_FOR));
        return  passwordRecordMono.map(passwordRestRecord ->
                        userSignUpService.changePassword(publicId, passwordRestRecord))
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> resendOtp(ServerRequest request)  {
        String email = request.pathVariable("email");
        String username = request.queryParam("username").orElse(email);
        log.info("Resend OTP Requested ", request.headers().firstHeader(X_FORWARD_FOR), username);
        return buildServerResponse(userSignUpService.resendOtp(username.trim(), email.trim(), otpTemplate));
    }

    public Mono<ServerResponse> sendOtp(ServerRequest request)  {
        String email = request.pathVariable("email");
        log.info("Send OTP Requested ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(userSignUpService.sendOtp(email.trim(), otpTemplate));
    }

    public Mono<ServerResponse> verifyOtp(ServerRequest request)  {
        String otp = request.pathVariable("otp");
        log.info("Verify user OTP Requested", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(userSignUpService.verifyOtp(otp));
    }

    public Mono<ServerResponse> disableUser(ServerRequest request)  {
        String publicId = request.pathVariable("publicId");
        log.info("Disable user Requested", request.headers().firstHeader(X_FORWARD_FOR), publicId);
        return buildServerResponse(userSignUpService.deActivateUser(publicId));
    }

    public Mono<ServerResponse> reEnableUser(ServerRequest request)  {
        String publicId = request.pathVariable("publicId");
        log.info("Re-activate user Requested", request.headers().firstHeader(X_FORWARD_FOR), " ", publicId);
        return buildServerResponse(userSignUpService.reActivateUser(publicId, otpTemplate, VerificationType.RE_ACTIVATE));
    }


    public Mono<ServerResponse> uploadDocument(ServerRequest request) {
        Mono<UploadFileRequest> base64 = request.bodyToMono(UploadFileRequest.class)
                .doOnNext(customValidator::validateEntries);
        log.info("Upload docs {} Requested", request.headers().firstHeader(X_FORWARD_FOR));
        return base64
                .map(meldFileProcessor::uploadImage)
                .flatMap(ApiResponse::buildServerResponse);
    }
}
