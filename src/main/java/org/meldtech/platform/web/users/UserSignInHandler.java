package org.meldtech.platform.web.users;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.config.CustomValidator;
import org.meldtech.platform.model.api.ApiResponse;
import org.meldtech.platform.model.api.request.UserRecord;
import org.meldtech.platform.service.ResourceOwnerService;
import org.meldtech.platform.service.UserSignUpService;
import org.meldtech.platform.service.token.TokenService;
import org.meldtech.platform.util.LoggerHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

import static org.meldtech.platform.model.api.ApiResponse.buildServerResponse;
import static org.meldtech.platform.model.api.ApiResponse.buildServerResponseNoBody;

@Service
@RequiredArgsConstructor
public class UserSignInHandler {
    private final LoggerHelper log = LoggerHelper.newInstance(UserSignInHandler.class.getName());
    private final ResourceOwnerService resourceOwnerService;
    private final TokenService tokenService;

    private static final String X_FORWARD_FOR = "X-Forwarded-For";

    public Mono<ServerResponse> getAuthorizeEndpoint(ServerRequest request)  {
        String clientIp = request.remoteAddress()
                .orElse(new InetSocketAddress("unknown", 0))
                .getHostString();
        log.info("Get Authorization code url requested ",  clientIp);
        return buildServerResponseNoBody(resourceOwnerService.requestAuthorizedUrl(clientIp));
    }

    public Mono<ServerResponse> requestAccessToken(ServerRequest request)  {
        String code = request.pathVariable("code");
        String clientIp = request.remoteAddress()
                .orElse(new InetSocketAddress("unknown", 0))
                .getHostString();
        log.info("Providing code for access token  Requested", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(tokenService.exchangeWithAccessToken(code, clientIp));
    }

    public Mono<ServerResponse> logout(ServerRequest request)  {
        String clientIp = request.remoteAddress()
                .orElse(new InetSocketAddress("unknown", 0))
                .getHostString();
        log.info("Logout from app Requested", request.headers().firstHeader(X_FORWARD_FOR), " ", clientIp);
        return buildServerResponseNoBody(resourceOwnerService.requestLogoutEndpoint());
    }




}