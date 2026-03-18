package org.meldtech.platform.model;

import org.meldtech.platform.model.api.response.FullUserProfileRecord;

import java.util.List;

public record CustomPagination(List<FullUserProfileRecord> results, int totalCount) {
}
