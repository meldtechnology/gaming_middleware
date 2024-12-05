package org.meldtech.platform.web.payments;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.config.CustomValidator;
import org.meldtech.platform.model.api.ApiResponse;
import org.meldtech.platform.model.api.AppResponse;
import org.meldtech.platform.model.api.request.ApplicantDocumentRequest;
import org.meldtech.platform.model.api.request.PaymentRequest;
import org.meldtech.platform.model.enums.PaymentStatusType;
import org.meldtech.platform.service.PaymentCallbackService;
import org.meldtech.platform.service.TransactionService;
import org.meldtech.platform.util.LoggerHelper;
import org.meldtech.platform.web.roles.RoleHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.meldtech.platform.model.api.ApiRequest.reportSettings;
import static org.meldtech.platform.model.api.ApiResponse.buildServerResponse;

@Service
@RequiredArgsConstructor
public class PaymentHandler {
    private final LoggerHelper log = LoggerHelper.newInstance(RoleHandler.class.getName());
    private final TransactionService transactionService;
    private final PaymentCallbackService callbackService;
    private final CustomValidator customValidator;

    private static final String X_FORWARD_FOR = "X-Forwarded-For";

    public Mono<ServerResponse> updateCallback(ServerRequest request)  {
        Mono<String> callbackData = request.bodyToMono(String.class);
        log.info("[{}] Get available roles Requested", request.headers().firstHeader(X_FORWARD_FOR));
        return callbackData
                .doOnNext(log::info)
                .map(callbackService::processCallback)
                .flatMap(ApiResponse::buildServerResponse);
    }

    public Mono<ServerResponse> updatePaymentWithReference(ServerRequest request)  {
        String externalReference = request.pathVariable("externalReference");
        log.info("[{}] Update payment status with external reference Requested from ",
                request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(callbackService.getPaymentStatus(externalReference));
    }

    public Mono<ServerResponse> getTransactionByReference(ServerRequest request)  {
        String reference = request.pathVariable("reference");
        log.info("Get transaction reference Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(transactionService.getTransaction(reference));
    }

    public Mono<ServerResponse> getTransactionByExtReference(ServerRequest request)  {
        String reference = request.pathVariable("reference");
        log.info("Get transaction by external reference Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(transactionService.getTransactionByExternalReference(reference));
    }

    public Mono<ServerResponse> getTransactionByStatus(ServerRequest request)  {
        String status = request.pathVariable("status");
        log.info("Get transactions by status "+status+"," +
                " Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(transactionService.getTransactionByStatus(
                PaymentStatusType.valueOf(status), reportSettings(request)));
    }

    public Mono<ServerResponse> getTransactionByDateRange(ServerRequest request)  {
        log.info("Get transactions by date range Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(transactionService.getTransactionByDateRange(reportSettings(request)));
    }

    public Mono<ServerResponse> createTransaction(ServerRequest request)  {
        Mono<PaymentRequest>  paymentRequest = request.bodyToMono(PaymentRequest.class)
                .doOnNext(customValidator::validateEntries);
        log.info("Create application Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return paymentRequest
                .map(transactionService::createTransaction)
                .flatMap(ApiResponse::buildServerResponse);
    }
}
