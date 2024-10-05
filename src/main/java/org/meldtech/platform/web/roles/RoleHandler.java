package org.meldtech.platform.web.roles;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.service.RoleService;
import org.meldtech.platform.service.UserProfileService;
import org.meldtech.platform.util.LoggerHelper;
import org.meldtech.platform.web.users.UserProfileHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.meldtech.platform.model.api.ApiRequest.reportSettings;
import static org.meldtech.platform.model.api.ApiResponse.buildServerResponse;

@Service
@RequiredArgsConstructor
public class RoleHandler {
    private final LoggerHelper log = LoggerHelper.newInstance(RoleHandler.class.getName());
    private final RoleService roleService;

    private static final String X_FORWARD_FOR = "X-Forwarded-For";

    public Mono<ServerResponse> getRoles(ServerRequest request)  {
        log.info("[{}] Get available roles Requested", request.headers().firstHeader(X_FORWARD_FOR));
        return buildServerResponse(roleService.getRoles(reportSettings(request)));
    }
}
