package org.meldtech.platform.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.meldtech.platform.model.UserInfo;
import org.meldtech.platform.model.api.request.DocumentFileRequest;
import org.meldtech.platform.model.api.request.DocumentTypeRequest;
import org.meldtech.platform.model.api.request.DocumentUpdateRequest;
import org.meldtech.platform.model.api.response.FullUserProfileRecord;
import org.meldtech.platform.model.enums.StatusType;
import org.meldtech.platform.util.ReportSettings;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestBodyHelper {

    public static FullUserProfileRecord reconstruct(String publicIs, FullUserProfileRecord record) {
        return FullUserProfileRecord.builder()
                .publicId(publicIs)
                .profile(record.profile())
                .build();
    }

    public static Mono<DocumentUpdateRequest> reconstruct(UserInfo userInfo, StatusType type, DocumentUpdateRequest request) {
        return Mono.just(
                new DocumentUpdateRequest(
                request.reference(),
                userInfo.username(),
                userInfo.publicId(),
                request.comment(),
                type.name())
        );
    }

    public static Instant[] getDate(ServerRequest request) {
        ReportSettings report = ReportSettings.instance()
                .start(request.queryParam("from").orElse("2024-01-01"))
                .end(request.queryParam("to").orElse("2024-12-31"));
        return new Instant[]{report.getStart(), report.getEnd()};
    }

    public static ReportSettings getReportSetting(ServerRequest request) {
        return ReportSettings.instance()
                .sortIn(request.queryParam("sortIn").orElse("asc"))
                .sortBy(request.queryParam("sortBy").orElse("createdOn"))
                .start(request.queryParam("from").orElse("2024-01-01"))
                .end(request.queryParam("to").orElse("2024-12-31"));
    }
    public static List<String> paramList(ServerRequest request, String name) {
        return List.of(request.queryParam(name).orElse("").split(","));
    }

    public static ReportSettings reportDuration(ServerRequest request) {
        return ReportSettings.instance()
                .start(request.queryParam("from").orElse("2024-01-01"))
                .end(request.queryParam("to").orElse("2024-12-31"));
    }

    public static Mono<DocumentTypeRequest> reconstruct(UserInfo userInfo, DocumentTypeRequest request) {
        return Mono.just(new DocumentTypeRequest(request.name(), request.description(), userInfo.username()));
    }

    public static Mono<DocumentFileRequest> reconstruct(UserInfo userInfo, DocumentFileRequest request) {
        return Mono.just(new DocumentFileRequest(request.code(),
                request.name(), request.description(), request.logo(), request.publicVisibility(),
                request.renewalName(), request.renewalDuration(), request.typeName(), request.feeType(),
                request.value(), request.flatFee(), request.formTemplate(), userInfo.username()));
    }
}
