package org.meldtech.platform.web.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.meldtech.platform.model.api.request.ApplicantDocumentRequest;
import org.meldtech.platform.model.api.request.DocumentFileRequest;
import org.meldtech.platform.model.api.request.DocumentTypeRequest;
import org.meldtech.platform.model.api.request.DocumentUpdateRequest;
import org.meldtech.platform.web.documents.*;
import org.meldtech.platform.web.documents.form.FormHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.meldtech.platform.web.config.AppRoutes.Document.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class WebDocumentConfig {

    // Document cycles
    @Bean
    @RouterOperations({
            @RouterOperation(path = DOCUMENT_CYCLE, produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET, beanClass = DocumentCycleHandler.class, beanMethod = "getDocumentCycles",
                    operation = @Operation( operationId = "getDocumentCycles", tags = "Documents Management API",
                            description = "Get Document Renewal Periods", summary = "Get Document Renewal Periods"
                    )
            ),
    })
    public RouterFunction<ServerResponse> cycleEndpointHandler(DocumentCycleHandler handler) {
        return route()
                .GET(DOCUMENT_CYCLE, accept(MediaType.APPLICATION_JSON), handler::getDocumentCycles)
                .build();
    }

    // Fee Type
    @Bean
    @RouterOperations({
            @RouterOperation(path = FEE_TYPE, produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET, beanClass = FeeTypeHandler.class, beanMethod = "getFeeTypes",
                    operation = @Operation( operationId = "getFeeTypes", tags = "Documents Management API",
                            description = "Get Fee Types", summary = "Get Fee Types"
                    )
            ),
    })
    public RouterFunction<ServerResponse> feeEndpointHandler(FeeTypeHandler handler) {
        return route()
                .GET(FEE_TYPE, accept(MediaType.APPLICATION_JSON), handler::getFeeTypes)
                .build();
    }

    // Document types
    @Bean
    @RouterOperations({
            @RouterOperation(path = DOCUMENT_TYPE, produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET, beanClass = DocumentTypeHandler.class, beanMethod = "getDocumentTypes",
                    operation = @Operation( operationId = "getDocumentTypes", tags = "Documents Management API",
                            description = "Get Document Types", summary = "Get Document Types",
                            parameters = {
                                    @Parameter(in = ParameterIn.QUERY, name = "page", required = true, example = "1"),
                                    @Parameter(in = ParameterIn.QUERY, name = "size", required = true, example = "10"),
                                    @Parameter(in = ParameterIn.QUERY, name = "sortBy"),
                                    @Parameter(in = ParameterIn.QUERY, name = "sortIn", example = "ASC"),
                                    @Parameter(in = ParameterIn.QUERY, name = "start"),
                                    @Parameter(in = ParameterIn.QUERY, name = "end")
                            })
            ),
            @RouterOperation(path = DOCUMENT_TYPE_NAME, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET, beanClass = DocumentTypeHandler.class, beanMethod = "getDocumentType",
                    operation = @Operation(operationId = "getDocumentType", tags = "Documents Management API",
                            description = "Get Document Type by Name", summary = "Get Document Type by Name",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "name")}
                    )
            ),
            @RouterOperation(path = DOCUMENT_TYPE, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST, beanClass = DocumentTypeHandler.class, beanMethod = "createType",
                    operation = @Operation(operationId = "createType", tags = "Documents Management API",
                            description = "Create Document Type", summary = "Create Document Type",
                            requestBody = @RequestBody(content = @Content(schema =
                            @Schema( implementation = DocumentTypeRequest.class)))
                    )
            ),
            @RouterOperation(path = DOCUMENT_TYPE_NAME, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT, beanClass = DocumentTypeHandler.class, beanMethod = "updateType",
                    operation = @Operation(operationId = "updateType", tags = "Documents Management API",
                            description = "Edit Document Type", summary = "Edit Document Type",
                            requestBody = @RequestBody(content = @Content(schema =
                            @Schema( implementation = DocumentTypeRequest.class))),
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "name")}
                    )
            ),
    })
    public RouterFunction<ServerResponse> typeEndpointHandler(DocumentTypeHandler handler) {
        return route()
                .GET(DOCUMENT_TYPE, accept(MediaType.APPLICATION_JSON), handler::getDocumentTypes)
                .GET(DOCUMENT_TYPE_NAME, accept(MediaType.APPLICATION_JSON), handler::getDocumentType)
                .POST(DOCUMENT_TYPE, accept(MediaType.APPLICATION_JSON)
                        .and(contentType(MediaType.APPLICATION_JSON)), handler::createType)
                .PUT(DOCUMENT_TYPE_NAME, accept(MediaType.APPLICATION_JSON)
                        .and(contentType(MediaType.APPLICATION_JSON)), handler::updateType)
                .build();
    }

    // Document types
    @Bean
    @RouterOperations({
            @RouterOperation(path = DOCUMENT_FILE, produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET, beanClass = DocumentFileHandler.class, beanMethod = "getDocumentFiles",
                    operation = @Operation( operationId = "getDocumentFiles", tags = "Documents Management API",
                            description = "Get Document Files", summary = "Get Document Files",
                            parameters = {
                                    @Parameter(in = ParameterIn.QUERY, name = "page", required = true, example = "1"),
                                    @Parameter(in = ParameterIn.QUERY, name = "size", required = true, example = "10"),
                                    @Parameter(in = ParameterIn.QUERY, name = "sortBy"),
                                    @Parameter(in = ParameterIn.QUERY, name = "sortIn", example = "ASC"),
                                    @Parameter(in = ParameterIn.QUERY, name = "start"),
                                    @Parameter(in = ParameterIn.QUERY, name = "end")
                            })
            ),
            @RouterOperation(path = DOCUMENT_FILE_NAME, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET, beanClass = DocumentFileHandler.class, beanMethod = "getDocumentFile",
                    operation = @Operation(operationId = "getDocumentFile", tags = "Documents Management API",
                            description = "Get Document File by Name", summary = "Get Document File by Name",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "name")}
                    )
            ),
            @RouterOperation(path = DOCUMENT_FILE_CODE, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET, beanClass = DocumentFileHandler.class, beanMethod = "getDocumentFileByCode",
                    operation = @Operation(operationId = "getDocumentFileByCode", tags = "Documents Management API",
                            description = "Get Document File by Code", summary = "Get Document File by Code",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "code")}
                    )
            ),
            @RouterOperation(path = DOCUMENT_FILE, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST, beanClass = DocumentFileHandler.class, beanMethod = "createFile",
                    operation = @Operation(operationId = "createFile", tags = "Documents Management API",
                            description = "Create Document File", summary = "Create Document File",
                            requestBody = @RequestBody(content = @Content(schema =
                            @Schema( implementation = DocumentFileRequest.class)))
                    )
            ),
            @RouterOperation(path = DOCUMENT_FILE_NAME, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT, beanClass = DocumentFileHandler.class, beanMethod = "updateFile",
                    operation = @Operation(operationId = "updateFile", tags = "Documents Management API",
                            description = "Edit Document File", summary = "Edit Document File",
                            requestBody = @RequestBody(content = @Content(schema =
                            @Schema( implementation = DocumentFileRequest.class))),
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "name")}
                    )
            ),
    })
    public RouterFunction<ServerResponse> fileEndpointHandler(DocumentFileHandler handler) {
        return route()
                .GET(DOCUMENT_FILE, accept(MediaType.APPLICATION_JSON), handler::getDocumentFiles)
                .GET(DOCUMENT_FILE_NAME, accept(MediaType.APPLICATION_JSON), handler::getDocumentFile)
                .GET(DOCUMENT_FILE_CODE, accept(MediaType.APPLICATION_JSON), handler::getDocumentFileByCode)
                .POST(DOCUMENT_FILE, accept(MediaType.APPLICATION_JSON)
                        .and(contentType(MediaType.APPLICATION_JSON)), handler::createFile)
                .PUT(DOCUMENT_FILE_NAME, accept(MediaType.APPLICATION_JSON)
                        .and(contentType(MediaType.APPLICATION_JSON)), handler::updateFile)
                .build();
    }

    // Document applications
    @Bean
    @RouterOperations({
            @RouterOperation(path = DOCUMENT_BASE, produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET, beanClass = ApplicationDocumentHandler.class, beanMethod = "getApplications",
                    operation = @Operation( operationId = "getApplications", tags = "Application Documents Management API",
                            description = "Get Application Documents", summary = "Get Application Documents",
                            parameters = {
                                    @Parameter(in = ParameterIn.QUERY, name = "page", required = true, example = "1"),
                                    @Parameter(in = ParameterIn.QUERY, name = "size", required = true, example = "10"),
                                    @Parameter(in = ParameterIn.QUERY, name = "sortBy"),
                                    @Parameter(in = ParameterIn.QUERY, name = "sortIn", example = "ASC"),
                                    @Parameter(in = ParameterIn.QUERY, name = "start"),
                                    @Parameter(in = ParameterIn.QUERY, name = "end")
                            })
            ),
            @RouterOperation(path = DOCUMENT_REFERENCE, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET, beanClass = ApplicationDocumentHandler.class, beanMethod = "getApplication",
                    operation = @Operation(operationId = "getApplication", tags = "Application Documents Management API",
                            description = "Get Application by reference", summary = "Get Application by reference",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "reference")}
                    )
            ),@RouterOperation(path = DOCUMENT_BY_STATUS, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET, beanClass = ApplicationDocumentHandler.class, beanMethod = "getApplicationsByStatus",
                    operation = @Operation(operationId = "getApplicationsByStatus", tags = "Application Documents Management API",
                            description = "Get Application by status", summary = "Get Application by status",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "status"),
                                    @Parameter(in = ParameterIn.QUERY, name = "page", required = true, example = "1"),
                                    @Parameter(in = ParameterIn.QUERY, name = "size", required = true, example = "10"),
                                    @Parameter(in = ParameterIn.QUERY, name = "sortBy"),
                                    @Parameter(in = ParameterIn.QUERY, name = "sortIn", example = "ASC")
                    })
            ),
            @RouterOperation(path = DOCUMENT_METRIC, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET, beanClass = ApplicationDocumentHandler.class, beanMethod = "getApplicationMetrics",
                    operation = @Operation(operationId = "getApplicationMetrics", tags = "Application Documents Management API",
                            description = "Get Application Metrics", summary = "Get Application Metrics"
                    )
            ),
            @RouterOperation(path = DOCUMENT_STATUS, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET, beanClass = ApplicationDocumentHandler.class, beanMethod = "getApplicationStatuses",
                    operation = @Operation(operationId = "getApplicationStatuses", tags = "Application Documents Management API",
                            description = "Get Application statuses", summary = "Get Application statuses"
                    )
            ),
            @RouterOperation(path = DOCUMENT_BASE, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST, beanClass = ApplicationDocumentHandler.class, beanMethod = "createApplication",
                    operation = @Operation(operationId = "createApplication", tags = "Application Documents Management API",
                            description = "Create Application", summary = "Create Application",
                            requestBody = @RequestBody(content = @Content(schema =
                            @Schema( implementation = ApplicantDocumentRequest.class)))
                    )
            ),
            @RouterOperation(path = DOCUMENT_REVIEW, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT, beanClass = ApplicationDocumentHandler.class, beanMethod = "reviewApplication",
                    operation = @Operation(operationId = "reviewApplication", tags = "Application Documents Management API",
                            description = "REVIEW application", summary = "REVIEW application",
                            requestBody = @RequestBody(content = @Content(schema =
                            @Schema( implementation = DocumentUpdateRequest.class)))
                    )
            ),
            @RouterOperation(path = DOCUMENT_DECLINE, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT, beanClass = ApplicationDocumentHandler.class, beanMethod = "declineApplication",
                    operation = @Operation(operationId = "declineApplication", tags = "Application Documents Management API",
                            description = "DECLINE application", summary = "DECLINE application",
                            requestBody = @RequestBody(content = @Content(schema =
                            @Schema( implementation = DocumentUpdateRequest.class)))
                    )
            ),
            @RouterOperation(path = DOCUMENT_APPROVE, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT, beanClass = ApplicationDocumentHandler.class, beanMethod = "approveApplication",
                    operation = @Operation(operationId = "approveApplication", tags = "Application Documents Management API",
                            description = "APPROVE application", summary = "APPROVE application",
                            requestBody = @RequestBody(content = @Content(schema =
                            @Schema( implementation = DocumentUpdateRequest.class)))
                    )
            ),
            @RouterOperation(path = DOCUMENT_ISSUE, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT, beanClass = ApplicationDocumentHandler.class, beanMethod = "issueApplication",
                    operation = @Operation(operationId = "issueApplication", tags = "Application Documents Management API",
                            description = "ISSUE license", summary = "ISSUE license",
                            requestBody = @RequestBody(content = @Content(schema =
                            @Schema( implementation = DocumentUpdateRequest.class)))
                    )
            ),
    })
    public RouterFunction<ServerResponse> applicationEndpointHandler(ApplicationDocumentHandler handler) {
        return route()
                .GET(DOCUMENT_BASE, accept(MediaType.APPLICATION_JSON), handler::getApplications)
                .GET(DOCUMENT_REFERENCE, accept(MediaType.APPLICATION_JSON), handler::getApplication)
                .GET(DOCUMENT_METRIC, accept(MediaType.APPLICATION_JSON), handler::getApplicationMetrics)
                .GET(DOCUMENT_STATUS, accept(MediaType.APPLICATION_JSON), handler::getApplicationStatuses)
                .GET(DOCUMENT_BY_STATUS, accept(MediaType.APPLICATION_JSON), handler::getApplicationsByStatus)
                .POST(DOCUMENT_BASE, accept(MediaType.APPLICATION_JSON)
                        .and(contentType(MediaType.APPLICATION_JSON)), handler::createApplication)
                .PUT(DOCUMENT_REVIEW, accept(MediaType.APPLICATION_JSON)
                        .and(contentType(MediaType.APPLICATION_JSON)), handler::reviewApplication)
                .PUT(DOCUMENT_DECLINE, accept(MediaType.APPLICATION_JSON)
                        .and(contentType(MediaType.APPLICATION_JSON)), handler::declineApplication)
                .PUT(DOCUMENT_APPROVE, accept(MediaType.APPLICATION_JSON)
                        .and(contentType(MediaType.APPLICATION_JSON)), handler::approveApplication)
                .PUT(DOCUMENT_ISSUE, accept(MediaType.APPLICATION_JSON)
                        .and(contentType(MediaType.APPLICATION_JSON)), handler::issueApplication)
                .build();
    }

    // Document Form
    @Bean
    @RouterOperations({
            @RouterOperation(path = DOCUMENT_FORM_TEMPLATE, produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET, beanClass = FormHandler.class, beanMethod = "getTemplate",
                    operation = @Operation( operationId = "getTemplate", tags = "Document Form Management API",
                            description = "Get Form Template", summary = "Get Form Template"
                    )
            ),
            @RouterOperation(path = DOCUMENT_FORM_COMPONENT, produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET, beanClass = FormHandler.class, beanMethod = "getComponent",
                    operation = @Operation( operationId = "getComponent", tags = "Document Form Management API",
                            description = "Get Form Components", summary = "Get Form Components"
                    )
            ),
    })
    public RouterFunction<ServerResponse> formEndpointHandler(FormHandler handler) {
        return route()
                .GET(DOCUMENT_FORM_TEMPLATE, accept(MediaType.APPLICATION_JSON), handler::getTemplate)
                .GET(DOCUMENT_FORM_COMPONENT, accept(MediaType.APPLICATION_JSON), handler::getComponent)
                .build();
    }

}
