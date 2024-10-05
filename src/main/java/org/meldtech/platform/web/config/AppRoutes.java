package org.meldtech.platform.web.config;

public interface AppRoutes {
    String API_V1_URL = "/v1";

    interface Client {
        String CLIENT_BASE = API_V1_URL + "/oauth2/clients";
        String CLIENT_BY_ID = CLIENT_BASE + "/{clientId}";
    }

    interface User {
        String USER_BASE = API_V1_URL + "/users";

        String USER_ADMIN_BASE =  USER_BASE + "/admin";
        String USER_PROFILE_ADMIN_BASE =  USER_ADMIN_BASE + "/profiles";

        String USER_PROFILE_BASE = USER_BASE + "/profiles";
        String USER_PROFILE_BY_ID = USER_BASE + "/profiles/{publicId}";

        String USER_SIGN_UP = USER_BASE + "/sign-up";
        String USER_RESEND_OTP = USER_BASE + "/{email}/send/otp";
        String USER_VERIFY_OTP = USER_BASE + "/verify/otp/{otp}";
        String USER_CHANGE_PASSWORD_PUBLIC_ID = USER_BASE + "/public/{email}/password";
        String USER_CHANGE_PASSWORD = USER_BASE + "/password";
        String USER_DISABLE = USER_BASE + "/de-activate/{publicId}";
    }

    interface Role {
        String ROLE_BASE = API_V1_URL + "/roles";
    }

    interface Document {
        String DOCUMENT_BASE = API_V1_URL + "/documents";
        String DOCUMENT_UPLOAD = DOCUMENT_BASE + "/upload";
    }

    interface Authentication {
        String BASE_AUTH = API_V1_URL + "/auth/users";
        String AUTHORIZE_URL = BASE_AUTH + "/authorize/endpoint";
        String AUTHORIZATION_CODE_URL = BASE_AUTH + "/token/endpoint/{code}";
        String LOGOUT_URL = BASE_AUTH + "/logout/endpoint";
    }
}
