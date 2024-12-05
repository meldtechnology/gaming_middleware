package org.meldtech.platform.web.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.meldtech.platform.model.api.request.PaymentRequest;
import org.meldtech.platform.web.documents.ApplicationDocumentHandler;
import org.meldtech.platform.web.payments.PaymentHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.meldtech.platform.web.config.AppRoutes.Document.*;
import static org.meldtech.platform.web.config.AppRoutes.Document.GENERATE_DOCUMENT_REFERENCE;
import static org.meldtech.platform.web.config.AppRoutes.Payment.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class WebPaymentConfig {
    @Bean
    @RouterOperations({
            @RouterOperation(path = GET_PAYMENT, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET, beanClass = PaymentHandler.class, beanMethod = "getTransactionByReference",
                    operation = @Operation(operationId = "getTransactionByReference", tags = "Transactions Management API",
                            description = "Get Transaction by reference", summary = "Get Transaction by reference",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "reference")
                    })
            ),
            @RouterOperation(path = GET_PAYMENT_EXTERNAL, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET, beanClass = PaymentHandler.class, beanMethod = "getTransactionByExtReference",
                    operation = @Operation(operationId = "getTransactionByExtReference", tags = "Transactions Management API",
                            description = "Get Transaction by external reference", summary = "Get Transaction by external reference",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "reference")
                    })
            ),
            @RouterOperation(path = GET_PAYMENT_STATUS, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET, beanClass = PaymentHandler.class, beanMethod = "getTransactionByStatus",
                    operation = @Operation(operationId = "getTransactionByStatus", tags = "Transactions Management API",
                            description = "Get Transactions by Status", summary = "Get Transactions by Status",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "status", example = "PENDING|PAID"),
                                    @Parameter(in = ParameterIn.QUERY, name = "page", required = true, example = "1"),
                                    @Parameter(in = ParameterIn.QUERY, name = "size", required = true, example = "10"),
                                    @Parameter(in = ParameterIn.QUERY, name = "sortBy"),
                                    @Parameter(in = ParameterIn.QUERY, name = "sortIn", example = "DESC")
                    })
            ),
            @RouterOperation(path = GET_PAYMENT_DATE_RANGE, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET, beanClass = PaymentHandler.class, beanMethod = "getTransactionByDateRange",
                    operation = @Operation(operationId = "getTransactionByDateRange", tags = "Transactions Management API",
                            description = "Get Transactions by Date", summary = "Get Transactions by Date",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "status"),
                                    @Parameter(in = ParameterIn.QUERY, name = "page", required = true, example = "1"),
                                    @Parameter(in = ParameterIn.QUERY, name = "size", required = true, example = "10"),
                                    @Parameter(in = ParameterIn.QUERY, name = "sortBy", example = "createdOn"),
                                    @Parameter(in = ParameterIn.QUERY, name = "sortIn", example = "DESC"),
                                    @Parameter(in = ParameterIn.QUERY, name = "start", example = "2024-10-1"),
                                    @Parameter(in = ParameterIn.QUERY, name = "end", example = "2024-12-31")
                    })
            ),
            @RouterOperation(path = PAYMENT_CALLBACK, produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST, beanClass = PaymentHandler.class, beanMethod = "updateCallback",
                    operation = @Operation(operationId = "updateCallback", tags = "Transactions Management API",
                            description = "Payment Webhook/Callback", summary = "Payment Webhook/Callback",
                            requestBody = @RequestBody(content = @Content(schema = @Schema( implementation = String.class)))
                    )
            ),
            @RouterOperation(path = CREATE_PAYMENT, produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST, beanClass = PaymentHandler.class, beanMethod = "createTransaction",
                    operation = @Operation(operationId = "createTransaction", tags = "Transactions Management API",
                            description = "Create Transaction", summary = "Create Transaction",
                            requestBody = @RequestBody(content = @Content(schema = @Schema( implementation = PaymentRequest.class)))
                    )
            ),
            @RouterOperation(path = GET_PAYMENT_EXTERNAL, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT, beanClass = PaymentHandler.class, beanMethod = "updatePaymentWithReference",
                    operation = @Operation(operationId = "updatePaymentWithReference", tags = "Transactions Management API",
                            description = "Update Payment status by reference", summary = "Update Payment status by reference",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "externalReference")
                            })
            ),
    })
    public RouterFunction<ServerResponse> paymentEndpointHandler(PaymentHandler handler) {
        return route()
                .POST(PAYMENT_CALLBACK, accept(MediaType.APPLICATION_JSON), handler::updateCallback)
                .POST(CREATE_PAYMENT, accept(MediaType.APPLICATION_JSON)
                        .and(contentType(MediaType.APPLICATION_JSON)), handler::createTransaction)
                .PUT(GET_PAYMENT_EXTERNAL, accept(MediaType.APPLICATION_JSON), handler::updatePaymentWithReference)
                .GET(GET_PAYMENT, accept(MediaType.APPLICATION_JSON), handler::getTransactionByReference)
                .GET(GET_PAYMENT_EXTERNAL, accept(MediaType.APPLICATION_JSON), handler::getTransactionByExtReference)
                .GET(GET_PAYMENT_STATUS, accept(MediaType.APPLICATION_JSON), handler::getTransactionByStatus)
                .GET(GET_PAYMENT_DATE_RANGE, accept(MediaType.APPLICATION_JSON), handler::getTransactionByDateRange)
                .build();
    }
}
