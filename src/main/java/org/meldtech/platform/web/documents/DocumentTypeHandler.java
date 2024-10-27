package org.meldtech.platform.web.documents;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.config.AuthTokenConfig;
import org.meldtech.platform.config.CustomValidator;
import org.meldtech.platform.model.api.ApiResponse;
import org.meldtech.platform.model.api.request.DocumentTypeRequest;
import org.meldtech.platform.service.DocumentTypeService;
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
public class DocumentTypeHandler {
    private final LoggerHelper log = LoggerHelper.newInstance(DocumentTypeHandler.class.getName());
    private final DocumentTypeService typeService;
    private final CustomValidator customValidator;

    private static final String X_FORWARD_FOR = "X-Forwarded-For";

    public Mono<ServerResponse> getDocumentTypes(ServerRequest request)  {
        log.info("Get available document types Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(typeService.getDocumentTypes(reportSettings(request)));
    }

    public Mono<ServerResponse> getDocumentType(ServerRequest request)  {
        String name = request.pathVariable("name");
        log.info("Get document type by name ", name, " Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(typeService.getDocumentType(name));
    }

    public Mono<ServerResponse> createType(ServerRequest request)  {
        Mono<DocumentTypeRequest>  typeRequestMono = request.bodyToMono(DocumentTypeRequest.class)
                .doOnNext(customValidator::validateEntries);
        Mono<JwtAuthenticationToken> jwtAuthToken = AuthTokenConfig.authenticatedToken(request);
        log.info("Create new document type Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return jwtAuthToken
                .map(ApiResponse::getUserInfoFromToken)
                .flatMap(userInfo -> typeRequestMono
                        .flatMap(typeRequest -> reconstruct(userInfo, typeRequest)))
                .map(typeService::createDocumentType)
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> updateType(ServerRequest request)  {
        Mono<DocumentTypeRequest>  typeRequestMono = request.bodyToMono(DocumentTypeRequest.class)
                .doOnNext(customValidator::validateEntries);
        String oldName = request.pathVariable("name");
        Mono<JwtAuthenticationToken> jwtAuthToken = AuthTokenConfig.authenticatedToken(request);
        log.info("Editing document type ", oldName, ", Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return jwtAuthToken
                .map(ApiResponse::getUserInfoFromToken)
                .flatMap(userInfo -> typeRequestMono
                        .flatMap(typeRequest -> reconstruct(userInfo, typeRequest)))
                .map(documentTypeRequest -> typeService.editDocumentType(oldName, documentTypeRequest))
                .flatMap(ApiResponse::buildServerResponse);
    }
}
