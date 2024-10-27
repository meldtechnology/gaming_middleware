package org.meldtech.platform.web.documents.form;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.config.CustomValidator;
import org.meldtech.platform.service.ApplicantDocumentService;
import org.meldtech.platform.service.FormFieldTypeService;
import org.meldtech.platform.service.FormTemplateService;
import org.meldtech.platform.util.LoggerHelper;
import org.meldtech.platform.web.documents.ApplicationDocumentHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.meldtech.platform.model.api.ApiResponse.buildServerResponse;

@Service
@RequiredArgsConstructor
public class FormHandler {
    private final LoggerHelper log = LoggerHelper.newInstance(FormHandler.class.getName());
    private final FormTemplateService  templateService;
    private final FormFieldTypeService formFieldTypeService;

    private static final String X_FORWARD_FOR = "X-Forwarded-For";

    public Mono<ServerResponse> getTemplate(ServerRequest request)  {
        log.info("Get form template Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(templateService.getFormTemplates());
    }

    public Mono<ServerResponse> getComponent(ServerRequest request)  {
        log.info("Get form fields Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(formFieldTypeService.getFormFieldTypes());
    }
}
