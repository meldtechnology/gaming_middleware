package org.meldtech.platform.web.report;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.constant.ReportType;
import org.meldtech.platform.model.api.ApiResponse;
import org.meldtech.platform.service.ApplicantDocumentService;
import org.meldtech.platform.service.DownloadGeneratorService;
import org.meldtech.platform.service.TransactionService;
import org.meldtech.platform.util.LoggerHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.meldtech.platform.helper.RequestBodyHelper.getReportSetting;
import static org.meldtech.platform.helper.RequestBodyHelper.paramList;

@Service
@RequiredArgsConstructor
public class ReportHandler {
    private final LoggerHelper log = LoggerHelper.newInstance(ReportHandler.class.getName());
    private final ApplicantDocumentService applicantDocumentService;
    private final TransactionService transactionService;
    private final DownloadGeneratorService downloadGeneratorService;

    private static final String X_FORWARD_FOR = "X-Forwarded-For";

    public Mono<ServerResponse> appWithDateRange(ServerRequest request)  {
        String reportType = request.queryParam("reportType").orElse("PDF");
        log.info("Generate application with date range Report Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return applicantDocumentService.getApplicationReportByDateRange(getReportSetting(request))
                .map(reportInfo -> downloadGeneratorService.generateReport(reportInfo, reportTpe(reportType)))
                .flatMap(streamApp ->  ApiResponse.buildServerResponseStreamBody(streamApp, reportTpe(reportType)));
    }

    public Mono<ServerResponse> appWithDateRangeFilter(ServerRequest request)  {
        String reportType = request.queryParam("reportType").orElse("PDF");
        log.info("Generate application with date range & Filter Report Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return applicantDocumentService.getApplicationReportByDateRangeAndFilter(getReportSetting(request), paramList(request, "status"))
                .map(reportInfo -> downloadGeneratorService.generateReport(reportInfo, reportTpe(reportType)))
                .flatMap(streamApp ->  ApiResponse.buildServerResponseStreamBody(streamApp, reportTpe(reportType)));
    }

    public Mono<ServerResponse> appWithDateRangeAndPaymentFilter(ServerRequest request)  {
        String reportType = request.queryParam("reportType").orElse("PDF");
        String status = request.queryParams().getFirst("status");
        log.info("Generate application with date range & Payment Filter Report Requested from ",
                request.headers().firstHeader(X_FORWARD_FOR));
        return applicantDocumentService.getApplicationReportByDateRangeAndPaymentStatus(getReportSetting(request), status)
                .map(reportInfo -> downloadGeneratorService.generateReport(reportInfo, reportTpe(reportType)))
                .flatMap(streamApp ->  ApiResponse.buildServerResponseStreamBody(streamApp, reportTpe(reportType)));
    }

    public Mono<ServerResponse>  paymentWithDateRange(ServerRequest request)  {
        String reportType = request.queryParam("reportType").orElse("PDF");
        log.info("Generate payment with date range Report Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return transactionService.getPaymentReportByDateRange(getReportSetting(request))
                .map(reportInfo -> downloadGeneratorService.generateReport(reportInfo, reportTpe(reportType)))
                .flatMap(streamApp ->  ApiResponse.buildServerResponseStreamBody(streamApp, reportTpe(reportType)));
    }

    public Mono<ServerResponse> paymentWithDateRangeAndPaymentFilter(ServerRequest request)  {
        String reportType = request.queryParam("reportType").orElse("PDF");
        String status = request.queryParams().getFirst("status");
        log.info("Generate payment with date range & Payment Filter Report Requested from ",
                request.headers().firstHeader(X_FORWARD_FOR));
        return transactionService.getPaymentReportByDateRangeAndStatus(getReportSetting(request), status)
                .map(reportInfo -> downloadGeneratorService.generateReport(reportInfo, reportTpe(reportType)))
                .flatMap(streamApp ->  ApiResponse.buildServerResponseStreamBody(streamApp, reportTpe(reportType)));
    }

    private ReportType reportTpe(String reportType) {
        return ReportType.valueOf(reportType.toUpperCase());
    }
}
