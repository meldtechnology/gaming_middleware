package org.meldtech.platform.web.documents;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.service.FeeTypeService;
import org.meldtech.platform.util.LoggerHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.meldtech.platform.model.api.ApiResponse.buildServerResponse;

@Service
@RequiredArgsConstructor
public class FeeTypeHandler {
    private final LoggerHelper log = LoggerHelper.newInstance(FeeTypeHandler.class.getName());
    private final FeeTypeService feeService;

    private static final String X_FORWARD_FOR = "X-Forwarded-For";

    public Mono<ServerResponse> getFeeTypes(ServerRequest request)  {
        log.info("Get available fee types Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(feeService.getFeeTypes());
    }
}
