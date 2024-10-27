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
        String EDIT_USER_PROFILE_ADMIN_BASE =  USER_ADMIN_BASE + "/profiles/{publicId}";
        String USER_PROFILE_METRIC = USER_ADMIN_BASE + "/metrics";
        String USER_SIGN_UP = USER_ADMIN_BASE + "/sign-up";
        String USER_CHANGE_PASSWORD_BY_ADMIN = USER_ADMIN_BASE + "/password/{publicId}";
        String CHANGE_USER_ROLE_BY_ADMIN = USER_ADMIN_BASE + "/{publicId}/role/{role}";
        String USER_DISABLE = USER_ADMIN_BASE + "/de-activate/{publicId}";
        String USER_ENABLE = USER_ADMIN_BASE + "/activate/{publicId}";

        String USER_PROFILE_BASE = USER_BASE + "/profiles";
        String USER_PROFILE_BY_ID = USER_BASE + "/profiles/{publicId}";

        String USER_RESEND_OTP = USER_BASE + "/public/{email}/send/otp";
        String USER_VERIFY_OTP = USER_BASE + "/verify/otp/{otp}";
        String USER_REQUEST_CHANGE_PASSWORD = USER_BASE + "/public/{email}/password/reset";
        String USER_CHANGE_PASSWORD = USER_BASE + "/password";
        String USER_CHANGE_PASSWORD_PUBLIC = USER_BASE + "/public/password/{otp}";

    }

    interface Role {
        String ROLE_BASE = API_V1_URL + "/roles";
    }

    interface Document {
        String DOCUMENT_BASE = API_V1_URL + "/documents";
        String DOCUMENT_UPLOAD = DOCUMENT_BASE + "/upload";

        String DOCUMENT_CYCLE = DOCUMENT_BASE + "/cycles";

        String DOCUMENT_TYPE = DOCUMENT_BASE + "/types";
        String DOCUMENT_TYPE_NAME = DOCUMENT_BASE + "/types/{name}";

        String DOCUMENT_FILE = DOCUMENT_BASE + "/files";
        String DOCUMENT_FILE_NAME = DOCUMENT_BASE + "/files/{name}";

        String DOCUMENT_REFERENCE = DOCUMENT_BASE + "/reference/{reference}";
        String DOCUMENT_METRIC = DOCUMENT_BASE + "/metrics";
        String DOCUMENT_STATUS = DOCUMENT_BASE + "/statues";
        String DOCUMENT_BY_STATUS = DOCUMENT_BASE + "/status/{status}";
        String DOCUMENT_REVIEW = DOCUMENT_BASE + "/REVIEW";
        String DOCUMENT_DECLINE = DOCUMENT_BASE + "/DECLINE";
        String DOCUMENT_APPROVE = DOCUMENT_BASE + "/APPROVE";
        String DOCUMENT_ISSUE = DOCUMENT_BASE + "/ISSUE";

        String DOCUMENT_FORM_TEMPLATE = DOCUMENT_BASE + "/template";
        String DOCUMENT_FORM_COMPONENT = DOCUMENT_BASE + "/components";
    }

    interface Authentication {
        String BASE_AUTH = API_V1_URL + "/auth/users";
        String AUTHORIZE_URL = BASE_AUTH + "/authorize/endpoint";
        String AUTHORIZATION_CODE_URL = BASE_AUTH + "/token/endpoint/{code}";
        String LOGOUT_URL = BASE_AUTH + "/logout/endpoint";
    }
}
