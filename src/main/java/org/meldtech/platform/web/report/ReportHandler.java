package org.meldtech.platform.web.report;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.model.api.ApiResponse;
import org.meldtech.platform.service.ApplicantDocumentService;
import org.meldtech.platform.service.DownloadGeneratorService;
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
    private final DownloadGeneratorService downloadGeneratorService;

    private static final String X_FORWARD_FOR = "X-Forwarded-For";

    public Mono<ServerResponse> appWithDateRange(ServerRequest request)  {
        log.info("Generate application with date range Report Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return applicantDocumentService.getApplicationReportByDateRange(getReportSetting(request))
                .map(downloadGeneratorService::generatePDF)
                .flatMap(ApiResponse::buildServerResponseStreamBody);
    }

    public Mono<ServerResponse> appWithDateRangeFilter(ServerRequest request)  {
        log.info("Generate application with date range & Filter Report Requested from ", request.headers().firstHeader(X_FORWARD_FOR));
        return applicantDocumentService.getApplicationReportByDateRangeAndFilter(getReportSetting(request), paramList(request, "status"))
                .map(downloadGeneratorService::generatePDF)
                .flatMap(ApiResponse::buildServerResponseStreamBody);
    }
}
