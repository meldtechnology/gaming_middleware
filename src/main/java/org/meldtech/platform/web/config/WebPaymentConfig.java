package org.meldtech.platform.web.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.meldtech.platform.web.payments.PaymentHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.meldtech.platform.web.config.AppRoutes.Payment.PAYMENT_CALLBACK;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class WebPaymentConfig {
    @Bean
    @RouterOperations({
            @RouterOperation(path = PAYMENT_CALLBACK, produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST, beanClass = PaymentHandler.class, beanMethod = "updateCallback",
                    operation = @Operation(operationId = "updateCallback", tags = "Payment Management API",
                            description = "Payment Webhook/Callback", summary = "Payment Webhook/Callback",
                            requestBody = @RequestBody(content = @Content(schema = @Schema( implementation = String.class)))
                    )
            ),
    })
    public RouterFunction<ServerResponse> paymentEndpointHandler(PaymentHandler handler) {
        return route()
                .POST(PAYMENT_CALLBACK, accept(MediaType.APPLICATION_JSON), handler::updateCallback)
                .build();
    }
}
