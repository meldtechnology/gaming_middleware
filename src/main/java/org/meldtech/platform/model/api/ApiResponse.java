package org.meldtech.platform.model.api;

import org.meldtech.platform.model.UserInfo;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @Author: Josiah Adetayo
 * @Email: josleke@gmail.com, josiah.adetayo@meld-tech.com
 * @Date: 12/15/23
 */
public class ApiResponse {
    private ApiResponse() {}

    public static Mono<ServerResponse> buildServerResponse(Mono<AppResponse> response) {
        try {
            return response
                    .flatMap(ServerResponse.ok()::bodyValue)
                    .switchIfEmpty(ServerResponse.badRequest().build());
        }catch (Exception e) {
            return ServerResponse.badRequest().build();
        }
    }

    public static Mono<ServerResponse> buildServerResponseNoBody(Mono<String> response) {
        try {
            return response
                    .flatMap(ServerResponse.ok()::bodyValue)
                    .switchIfEmpty(ServerResponse.badRequest().build());
        }catch (Exception e) {
            return ServerResponse.badRequest().build();
        }
    }

    public static String getPublicIdFromToken(JwtAuthenticationToken jwtToken) {
        return jwtToken.getTokenAttributes()
                .getOrDefault("publicId", "")
                .toString();
    }

    public static UserInfo getUserInfoFromToken(JwtAuthenticationToken jwtToken) {
        return UserInfo.builder()
                .username(jwtToken.getTokenAttributes()
                        .getOrDefault("sub", "")
                        .toString())
                .publicId(jwtToken.getTokenAttributes()
                        .getOrDefault("publicId", "")
                        .toString())
                .build();
    }
}
