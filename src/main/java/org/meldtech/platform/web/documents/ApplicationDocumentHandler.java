package org.meldtech.platform.web.documents;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.config.AuthTokenConfig;
import org.meldtech.platform.config.CustomValidator;
import org.meldtech.platform.model.api.ApiResponse;
import org.meldtech.platform.model.api.request.ApplicantDocumentRequest;
import org.meldtech.platform.model.api.request.DocumentUpdateRequest;
import org.meldtech.platform.model.enums.StatusType;
import org.meldtech.platform.service.ApplicantDocumentService;
import org.meldtech.platform.util.LoggerHelper;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.meldtech.platform.helper.RequestBodyHelper.getDate;
import static org.meldtech.platform.helper.RequestBodyHelper.reconstruct;
import static org.meldtech.platform.model.api.ApiRequest.reportSettings;
import static org.meldtech.platform.model.api.ApiResponse.buildServerResponse;

@Service
@RequiredArgsConstructor
public class ApplicationDocumentHandler {
    private final LoggerHelper log = LoggerHelper.newInstance(ApplicationDocumentHandler.class.getName());
    private final ApplicantDocumentService documentService;
    private final CustomValidator customValidator;

    private static final String X_FORWARD_FOR = "X-Forwarded-For";

    public Mono<ServerResponse> getApplications(ServerRequest request)  {
        log.info("Get application documents Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return getOrDateRangeApplications(request);
    }

    public Mono<ServerResponse> getApplication(ServerRequest request)  {
        String reference = request.pathVariable("reference");
        log.info("Get application document Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(documentService.getApplications(reference));
    }

    public Mono<ServerResponse> getApplicationsByStatus(ServerRequest request)  {
        String status = request.pathVariable("status");
        log.info("Get application documents by status "+status+"," +
                " Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(documentService.getApplicationsByStatus(status, reportSettings(request)));
    }

    public Mono<ServerResponse> getApplicationMetrics(ServerRequest request)  {
        log.info("Get application metrics Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(documentService.getMetrics());
    }

    public Mono<ServerResponse> getApplicationStatuses(ServerRequest request)  {
        log.info("Get application statuses Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(documentService.getStatuses());
    }

    public Mono<ServerResponse> generateApplicationReference(ServerRequest request)  {
        log.info("Generate application reference Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(documentService.generateReference());
    }

    public Mono<ServerResponse> createApplication(ServerRequest request)  {
        Mono<ApplicantDocumentRequest>  documentRequestMono = request.bodyToMono(ApplicantDocumentRequest.class)
                .doOnNext(customValidator::validateEntries);
        log.info("Create application Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return documentRequestMono
                .map(documentService::createApplicantDocument)
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> reviewApplication(ServerRequest request)  {
        Mono<DocumentUpdateRequest>  documentRequestMono = request.bodyToMono(DocumentUpdateRequest.class)
                .doOnNext(customValidator::validateEntries);
        Mono<JwtAuthenticationToken> jwtAuthToken = AuthTokenConfig.authenticatedToken(request);
        log.info("Transit application to REVIEW Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return jwtAuthToken
                .map(ApiResponse::getUserInfoFromToken)
                .flatMap(userInfo -> documentRequestMono
                        .flatMap(docRequest -> reconstruct(userInfo, StatusType.REVIEW, docRequest)))
                .map(documentService::updateApplicantDocument)
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> declineApplication(ServerRequest request)  {
        Mono<DocumentUpdateRequest>  documentRequestMono = request.bodyToMono(DocumentUpdateRequest.class)
                .doOnNext(customValidator::validateEntries);
        Mono<JwtAuthenticationToken> jwtAuthToken = AuthTokenConfig.authenticatedToken(request);
        log.info("Transit application to DECLINE Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return jwtAuthToken
                .map(ApiResponse::getUserInfoFromToken)
                .flatMap(userInfo -> documentRequestMono
                        .flatMap(docRequest -> reconstruct(userInfo, StatusType.DECLINE, docRequest)))
                .map(documentService::updateApplicantDocument)
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> approveApplication(ServerRequest request)  {
        Mono<DocumentUpdateRequest>  documentRequestMono = request.bodyToMono(DocumentUpdateRequest.class)
                .doOnNext(customValidator::validateEntries);
        Mono<JwtAuthenticationToken> jwtAuthToken = AuthTokenConfig.authenticatedToken(request);
        log.info("Transit application to APPROVE Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return jwtAuthToken
                .map(ApiResponse::getUserInfoFromToken)
                .flatMap(userInfo -> documentRequestMono
                        .flatMap(docRequest -> reconstruct(userInfo, StatusType.APPROVE, docRequest)))
                .map(documentService::updateApplicantDocument)
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> issueApplication(ServerRequest request)  {
        Mono<DocumentUpdateRequest>  documentRequestMono = request.bodyToMono(DocumentUpdateRequest.class)
                .doOnNext(customValidator::validateEntries);
        Mono<JwtAuthenticationToken> jwtAuthToken = AuthTokenConfig.authenticatedToken(request);
        log.info("Transit application to ISSUED Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return jwtAuthToken
                .map(ApiResponse::getUserInfoFromToken)
                .flatMap(userInfo -> documentRequestMono
                        .flatMap(docRequest -> reconstruct(userInfo, StatusType.ISSUED, docRequest)))
                .map(documentService::updateApplicantDocument)
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> getAppLicenseMetrics(ServerRequest request)  {
        log.info("Get Issued License Metrics Requested ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(documentService.getLicenseMetrics());
    }

    private Mono<ServerResponse> getOrDateRangeApplications(ServerRequest request) {
        return request.queryParam("from").isPresent() ?
                buildServerResponse(documentService.getApplicationsByDatRanges(
                getDate(request)[0],
                getDate(request)[1],
                reportSettings(request))) :
                buildServerResponse(documentService.getApplications(reportSettings(request)));
    }
}
