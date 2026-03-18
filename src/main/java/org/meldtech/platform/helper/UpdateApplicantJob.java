package org.meldtech.platform.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.meldtech.platform.model.CustomPagination;
import org.meldtech.platform.model.api.response.FullUserProfileRecord;
import org.meldtech.platform.service.ApplicantNameUpdateService;
import org.meldtech.platform.service.UserProfileService;
import org.meldtech.platform.util.ReportSettings;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UpdateApplicantJob {
    private final ApplicantNameUpdateService service;
    private final UserProfileService userProfileService;
    private final ObjectMapper objectMapper;

    private boolean isExecuted = false;

    // Runs once 5 seconds after startup
    @Scheduled(initialDelay = 5000, fixedDelay = Long.MAX_VALUE)
    public void runOnce() {
        if (!isExecuted) {
            System.out.println("Running one-time scheduled task...");
            isExecuted = true;
            updateApplicantName();
        }
    }

    private void updateApplicantName() {
        ReportSettings reportSettings = ReportSettings.instance().page(1).size(100);
        userProfileService.getUserProfiles(reportSettings)
                .flatMap(appResponse -> {
                    List<FullUserProfileRecord> users = convert(appResponse.getData());
                    var mappedUsers = getUsersMap(users);
                    service.updateApplicantNames(mappedUsers);
                    return Mono.just(users);
                }).subscribe();
    }

    private List<FullUserProfileRecord> convert(Object records) {
        CustomPagination pagination = objectMapper.convertValue(records, CustomPagination.class);
        return pagination.results();
    }

    private Map<String, String> getUsersMap(List<FullUserProfileRecord> users) {
        return users.stream()
                .collect(HashMap::new, (m, u) ->
                        m.put(u.publicId(), u.profile().firstName() + " " + u.profile().lastName()), HashMap::putAll);
    }

}
