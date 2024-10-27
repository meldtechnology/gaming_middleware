package org.meldtech.platform.web.documents;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.service.DocumentCycleService;
import org.meldtech.platform.util.LoggerHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.meldtech.platform.model.api.ApiResponse.buildServerResponse;

@Service
@RequiredArgsConstructor
public class DocumentCycleHandler {
    private final LoggerHelper log = LoggerHelper.newInstance(DocumentCycleHandler.class.getName());
    private final DocumentCycleService cycleService;

    private static final String X_FORWARD_FOR = "X-Forwarded-For";

    public Mono<ServerResponse> getDocumentCycles(ServerRequest request)  {
        log.info("Get available document cycles Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(cycleService.getDocumentCycles());
    }
}
