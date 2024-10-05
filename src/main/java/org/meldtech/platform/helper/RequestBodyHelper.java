package org.meldtech.platform.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.meldtech.platform.model.api.response.FullUserProfileRecord;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestBodyHelper {

    public static FullUserProfileRecord reconstruct(String publicIs, FullUserProfileRecord record) {
        return FullUserProfileRecord.builder()
                .publicId(publicIs)
                .profile(record.profile())
                .build();
    }
}
