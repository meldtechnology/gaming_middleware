package org.meldtech.platform.web.payments;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.model.api.ApiResponse;
import org.meldtech.platform.model.api.AppResponse;
import org.meldtech.platform.util.LoggerHelper;
import org.meldtech.platform.web.roles.RoleHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentHandler {
    private final LoggerHelper log = LoggerHelper.newInstance(RoleHandler.class.getName());

    private static final String X_FORWARD_FOR = "X-Forwarded-For";

    public Mono<ServerResponse> updateCallback(ServerRequest request)  {
        Mono<String> callbackData = request.bodyToMono(String.class);
        log.info("[{}] Get available roles Requested", request.headers().firstHeader(X_FORWARD_FOR));
        return callbackData
                .doOnNext(log::info)
                .map(data -> Mono.just(AppResponse.builder()
                        .status(true)
                        .message("Done processing")
                        .data("{}")
                        .build()) )
                .flatMap(ApiResponse::buildServerResponse);
    }
}
