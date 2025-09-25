package org.meldtech.platform.web.authorizer;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.config.CustomValidator;
import org.meldtech.platform.model.api.ApiResponse;
import org.meldtech.platform.model.dto.OAuth2RegisteredClientRecord;
import org.meldtech.platform.service.OAuth2RegisteredClientService;
import org.meldtech.platform.util.LoggerHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.meldtech.platform.model.api.ApiRequest.reportSettings;
import static org.meldtech.platform.model.api.ApiResponse.buildServerResponse;


@Service
@RequiredArgsConstructor
public class AuthorizerHandler {
    private final LoggerHelper log = LoggerHelper.newInstance(AuthorizerHandler.class.getName());
    private final OAuth2RegisteredClientService clientService;
    private final CustomValidator validator;

    @Value("${auth.config.disabled}")
    private boolean disabled;

    private static final String X_FORWARD_FOR = "X-Forwarded-For";

    public Mono<ServerResponse> addClient(ServerRequest request)  {
        Mono<OAuth2RegisteredClientRecord> recordMono = request.bodyToMono(OAuth2RegisteredClientRecord.class)
                .doOnNext(validator::validateEntries);
        log.info("Creating client record Requested ", request.headers().firstHeader(X_FORWARD_FOR));
        if(disabled) return endpointDisabled();
        return recordMono
                .doOnNext(log::info)
                .map(clientService::createClient)
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> getClientById(ServerRequest request)  {
        String clientId = request.pathVariable("clientId");
        log.info("Get Client Requested ", request.headers().firstHeader(X_FORWARD_FOR), " ", clientId);
        if(disabled) return endpointDisabled();
        return buildServerResponse(clientService.getClients(clientId));
    }

    public Mono<ServerResponse> getAllClient(ServerRequest request)  {
        log.info("[{}] Get all client Requested", request.headers().firstHeader(X_FORWARD_FOR));
        if(disabled) return endpointDisabled();
        return buildServerResponse(clientService.getClients(reportSettings(request)));
    }

    public Mono<ServerResponse> updateClient(ServerRequest request)  {
        String clientId = request.pathVariable("clientId");
        log.info("[{}] Update client Requested, {}", request.headers().firstHeader(X_FORWARD_FOR), clientId);
        if(disabled) return endpointDisabled();
        return request.bodyToMono(OAuth2RegisteredClientRecord.class)
                .map(record -> clientService.updateClients(clientId, record))
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> deleteClient(ServerRequest request)  {
        String clientId = request.pathVariable("clientId");
        log.info("[{}] Delete client Requested, {}", request.headers().firstHeader(X_FORWARD_FOR), clientId);
        if(disabled) return endpointDisabled();
        return buildServerResponse(clientService.deleteClient(clientId));
    }

    private Mono<ServerResponse> endpointDisabled() {
        return buildServerResponse(Mono.empty());
    }
}
