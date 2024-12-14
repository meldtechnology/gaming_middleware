package org.meldtech.platform.web.users;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.model.api.ApiResponse;
import org.meldtech.platform.model.constant.VerifyType;
import org.meldtech.platform.model.dto.company.VerificationRequest;
import org.meldtech.platform.service.CompanyService;
import org.meldtech.platform.util.LoggerHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.meldtech.platform.model.api.ApiResponse.buildServerResponse;

@Service
@RequiredArgsConstructor
public class IdentityVerifierHandler {
    private final LoggerHelper log = LoggerHelper.newInstance(IdentityVerifierHandler.class.getName());
    private final CompanyService companyService;

    private static final String X_FORWARD_FOR = "X-Forwarded-For";

    public Mono<ServerResponse> verifyCompany(ServerRequest request)  {
        Mono<VerificationRequest> verifyRequest = request.bodyToMono(VerificationRequest.class);
        String type = request.queryParam("type").orElse("CAC");
        log.info("verify Proprietor/Agent cac/nin Requested ", request.headers().firstHeader(X_FORWARD_FOR));
        return verifyRequest
                .map(request1 -> companyService.verifyIdentity(request1, VerifyType.valueOf(type)))
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> getEntityMetrics(ServerRequest request)  {
        log.info("Get Operator Metrics Requested ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(companyService.getTotalOperator());
    }
}
