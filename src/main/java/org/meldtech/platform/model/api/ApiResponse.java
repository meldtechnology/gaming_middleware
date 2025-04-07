package org.meldtech.platform.model.api;

import org.meldtech.platform.constant.ReportType;
import org.meldtech.platform.model.UserInfo;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    public static Mono<ServerResponse> buildServerResponseTestBody(Mono<AppResponse> response) {
        try {
            return response
                    .map(appResponse -> appResponse.getData().toString())
                    .flatMap(ServerResponse.ok()::bodyValue)
                    .switchIfEmpty(ServerResponse.badRequest().build());
        }catch (Exception e) {
            return ServerResponse.badRequest().build();
        }
    }

    public static Mono<ServerResponse> buildServerResponseStreamBody(Mono<byte[]> response, ReportType streamType) {
        try {
            return response
                    .map(appResponse -> appResponse)
                    .flatMap(ServerResponse.ok().headers(httpHeaders -> {
                        getHttpHeaders(httpHeaders, streamType);
                    })::bodyValue)
                    .switchIfEmpty(ServerResponse.badRequest().build());
        }catch (Exception e) {
            return ServerResponse.badRequest().build();
        }
    }

    private static void getHttpHeaders(HttpHeaders httpHeaders, ReportType reportType) {
        if(reportType == ReportType.PDF) {
            httpHeaders.setContentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE));
            httpHeaders.setContentDisposition(ContentDisposition.attachment().filename("report.pdf").build());
        }else if(reportType == ReportType.CSV) {
            httpHeaders.setContentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE));
            httpHeaders.setContentDisposition(ContentDisposition.attachment().filename("report.csv").build());
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
