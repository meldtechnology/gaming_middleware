package org.meldtech.platform.web.users;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.config.AuthTokenConfig;
import org.meldtech.platform.config.CustomValidator;
import org.meldtech.platform.helper.RequestBodyHelper;
import org.meldtech.platform.model.api.ApiResponse;
import org.meldtech.platform.model.api.request.UserProfileRecord;
import org.meldtech.platform.model.api.response.FullUserProfileRecord;
import org.meldtech.platform.service.OAuth2RegisteredClientService;
import org.meldtech.platform.service.UserProfileService;
import org.meldtech.platform.util.LoggerHelper;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.meldtech.platform.model.api.ApiRequest.reportSettings;
import static org.meldtech.platform.model.api.ApiResponse.buildServerResponse;

@Service
@RequiredArgsConstructor
public class UserProfileHandler {
    private final LoggerHelper log = LoggerHelper.newInstance(UserProfileHandler.class.getName());
    private final UserProfileService userProfileService;
    private final CustomValidator customValidator;

    private static final String X_FORWARD_FOR = "X-Forwarded-For";


    public Mono<ServerResponse> getProfiles(ServerRequest request)  {
        log.info("[{}] Get user profiles Requested", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(userProfileService.getUserProfiles(reportSettings(request)));
    }

    public Mono<ServerResponse> getUserProfile(ServerRequest request)  {
        Mono<JwtAuthenticationToken> jwtAuthToken = AuthTokenConfig.authenticatedToken(request);
        log.info("[{}] Get user profile Requested", request.headers().firstHeader(X_FORWARD_FOR));
        return jwtAuthToken
                .map(ApiResponse::getPublicIdFromToken)
                .map(userProfileService::getUserProfile)
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> getUserProfileByAdmin(ServerRequest request)  {
        String userPublicId = request.pathVariable("publicId");
        log.info("[{}] Get user profile By Admin Requested", request.headers().firstHeader(X_FORWARD_FOR));
        return Mono.just(userPublicId)
                .map(userProfileService::getUserProfileByAdmin)
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> addUserProfile(ServerRequest request)  {
        Mono<FullUserProfileRecord> profileRecordMono = request.bodyToMono(FullUserProfileRecord.class)
                .doOnNext(customValidator::validateEntries);
        Mono<JwtAuthenticationToken> jwtAuthToken = AuthTokenConfig.authenticatedToken(request);
        log.info("[{}] Create new user profile Requested", request.headers().firstHeader(X_FORWARD_FOR));
        return jwtAuthToken
                .map(ApiResponse::getPublicIdFromToken)
                .map(publicId -> profileRecordMono
                        .flatMap(userProfileRecord -> userProfileService.createUserProfile(
                                RequestBodyHelper.reconstruct(publicId, userProfileRecord))) )
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> editUserProfile(ServerRequest request)  {
        Mono<FullUserProfileRecord> profileRecordMono = request.bodyToMono(FullUserProfileRecord.class)
                .doOnNext(customValidator::validateEntries);
        Mono<JwtAuthenticationToken> jwtAuthToken = AuthTokenConfig.authenticatedToken(request);
        log.info("[{}] Edit user profile Requested", request.headers().firstHeader(X_FORWARD_FOR));
        return jwtAuthToken
                .map(ApiResponse::getPublicIdFromToken)
                .map(publicId -> profileRecordMono
                        .flatMap(userProfileRecord -> userProfileService.updateUserProfile(
                                RequestBodyHelper.reconstruct(publicId, userProfileRecord))) )
                .flatMap(ApiResponse::buildServerResponse);
    }
}