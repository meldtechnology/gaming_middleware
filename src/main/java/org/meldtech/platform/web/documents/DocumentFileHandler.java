package org.meldtech.platform.web.documents;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.config.AuthTokenConfig;
import org.meldtech.platform.config.CustomValidator;
import org.meldtech.platform.model.api.ApiResponse;
import org.meldtech.platform.model.api.request.DocumentFileRequest;
import org.meldtech.platform.service.DocumentFileService;
import org.meldtech.platform.util.LoggerHelper;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.meldtech.platform.helper.RequestBodyHelper.reconstruct;
import static org.meldtech.platform.model.api.ApiRequest.reportSettings;
import static org.meldtech.platform.model.api.ApiResponse.buildServerResponse;

@Service
@RequiredArgsConstructor
public class DocumentFileHandler {
    private final LoggerHelper log = LoggerHelper.newInstance(DocumentFileHandler.class.getName());
    private final DocumentFileService fileService;
    private final CustomValidator customValidator;

    private static final String X_FORWARD_FOR = "X-Forwarded-For";

    public Mono<ServerResponse> getDocumentFiles(ServerRequest request)  {
        log.info("Get available document files Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(fileService.getDocumentFiles(reportSettings(request)));
    }

    public Mono<ServerResponse> getDocumentFile(ServerRequest request)  {
        String name = request.pathVariable("name");
        log.info("Get document file by name ", name, " Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(fileService.getDocumentFile(name));
    }

    public Mono<ServerResponse> getDocumentFilesWithFilter(ServerRequest request)  {
       String categoryFilter = request.queryParam("categoryFilter").orElse("");
        log.info("Get available files by filter, Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(fileService.getDocumentFilesWithFilter(categoryFilter, reportSettings(request)));
    }

    public Mono<ServerResponse> getDocumentFileByCode(ServerRequest request)  {
        String code = request.pathVariable("code");
        log.info("Get document file by name ", code, " Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(fileService.getDocumentFileByCode(code));
    }

    public Mono<ServerResponse> createFile(ServerRequest request)  {
        Mono<DocumentFileRequest>  fileRequestMono = request.bodyToMono(DocumentFileRequest.class)
                .doOnNext(customValidator::validateEntries);
        Mono<JwtAuthenticationToken> jwtAuthToken = AuthTokenConfig.authenticatedToken(request);
        log.info("Create new document file Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return jwtAuthToken
                .map(ApiResponse::getUserInfoFromToken)
                .flatMap(userInfo -> fileRequestMono
                        .flatMap(typeRequest -> reconstruct(userInfo, typeRequest)))
                .map(fileService::createDocumentFile)
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> updateFile(ServerRequest request)  {
        Mono<DocumentFileRequest>  fileRequestMono = request.bodyToMono(DocumentFileRequest.class)
                .doOnNext(customValidator::validateEntries);
        String oldName = request.pathVariable("name");
        Mono<JwtAuthenticationToken> jwtAuthToken = AuthTokenConfig.authenticatedToken(request);
        log.info("Editing document file ", oldName, ", Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return jwtAuthToken
                .map(ApiResponse::getUserInfoFromToken)
                .flatMap(userInfo -> fileRequestMono
                        .flatMap(typeRequest -> reconstruct(userInfo, typeRequest)))
                .map(DocumentFileRequest -> fileService.editDocumentFile(oldName, DocumentFileRequest))
                .flatMap(ApiResponse::buildServerResponse);
    }
}
