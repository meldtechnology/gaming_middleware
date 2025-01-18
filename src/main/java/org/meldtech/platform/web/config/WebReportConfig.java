package org.meldtech.platform.web.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.meldtech.platform.model.api.request.PaymentRequest;
import org.meldtech.platform.web.payments.PaymentHandler;
import org.meldtech.platform.web.report.ReportHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.meldtech.platform.web.config.AppRoutes.Payment.*;
import static org.meldtech.platform.web.config.AppRoutes.Reporting.APPLICATION_FILTER_REPORT;
import static org.meldtech.platform.web.config.AppRoutes.Reporting.APPLICATION_REPORT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class WebReportConfig {
    @Bean
    @RouterOperations({
            @RouterOperation(path = APPLICATION_REPORT, produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE},
                    method = RequestMethod.GET, beanClass = ReportHandler.class, beanMethod = "appWithDateRange",
                    operation = @Operation(operationId = "appWithDateRange", tags = "Report Management API",
                            description = "Get Application Report by Date Range", summary = "Get Application Report by Date Range",
                            parameters = {
                                    @Parameter(in = ParameterIn.QUERY, name = "start", required = true),
                                    @Parameter(in = ParameterIn.QUERY, name = "end", required = true),
                                    @Parameter(in = ParameterIn.QUERY, name = "sortBy"),
                                    @Parameter(in = ParameterIn.QUERY, name = "sortIn", example = "DESC")
                    })
            ),
            @RouterOperation(path = APPLICATION_FILTER_REPORT, produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE},
                    method = RequestMethod.GET, beanClass = ReportHandler.class, beanMethod = "appWithDateRangeFilter",
                    operation = @Operation(operationId = "appWithDateRangeFilter", tags = "Report Management API",
                            description = "Get Application Report by Date Range and Filter",
                            summary = "Get Application Report by Date Range and filter",
                            parameters = {
                                    @Parameter(in = ParameterIn.QUERY, name = "start", required = true),
                                    @Parameter(in = ParameterIn.QUERY, name = "end", required = true),
                                    @Parameter(in = ParameterIn.QUERY, name = "sortBy"),
                                    @Parameter(in = ParameterIn.QUERY, name = "sortIn", example = "DESC"),
                                    @Parameter(in = ParameterIn.QUERY, name = "status", required = true, example = "PENDING, REVIEW")
                    })
            ),
    })
    public RouterFunction<ServerResponse> reportEndpointHandler(ReportHandler handler) {
        return route()
                .GET(APPLICATION_REPORT, accept(MediaType.APPLICATION_OCTET_STREAM), handler::appWithDateRange)
                .GET(APPLICATION_FILTER_REPORT, accept(MediaType.APPLICATION_OCTET_STREAM), handler::appWithDateRangeFilter)
                .build();
    }
}
