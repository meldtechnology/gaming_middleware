package org.meldtech.platform.model;

import lombok.Builder;

@Builder
public record UserInfo(String username, String publicId) {
}
