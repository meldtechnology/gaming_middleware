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
        String USER_PERMISSION_BASE = USER_BASE + "/permissions";
        String USER_PROFILE_BY_ID = USER_BASE + "/profiles/{publicId}";
        String ENTITY_VERIFY = USER_BASE + "/verify/identity";
        String ENTITY_METRICS = USER_PROFILE_BASE + "/entity/metrics";

        String USER_SIGN_UP_PUBLIC = USER_BASE + "/public/sign-up";
        String USER_SEND_OTP = USER_BASE + "/public/email/{email}/send/otp";
        String USER_RESEND_OTP = USER_BASE + "/public/{email}/send/otp";
        String USER_VERIFY_OTP = USER_BASE + "/verify/otp/{otp}";
        String USER_REQUEST_CHANGE_PASSWORD = USER_BASE + "/public/{email}/password/reset";
        String USER_CHANGE_PASSWORD = USER_BASE + "/password";
        String USER_CHANGE_PASSWORD_PUBLIC_ID = USER_BASE + "/public/password/publicId/{publicId}";
        String USER_CHANGE_PASSWORD_PUBLIC = USER_BASE + "/public/password/{otp}";

    }

    interface Role {
        String ROLE_BASE = API_V1_URL + "/roles";
    }

    interface Document {
        String DOCUMENT_BASE = API_V1_URL + "/documents";
        String DOCUMENT_UPLOAD = DOCUMENT_BASE + "/upload";

        String DOCUMENT_CYCLE = DOCUMENT_BASE + "/cycles";
        String FEE_TYPE = DOCUMENT_BASE + "/fees/type";

        String DOCUMENT_TYPE = DOCUMENT_BASE + "/types";
        String DOCUMENT_TYPE_NAME = DOCUMENT_BASE + "/types/{name}";

        String DOCUMENT_FILE = DOCUMENT_BASE + "/files";
        String DOCUMENT_FILE_PUBLIC = DOCUMENT_BASE + "/public/files";
        String DOCUMENT_FILE_PUBLIC_WITH_FILTER = DOCUMENT_BASE + "/public/filter/files";
        String DOCUMENT_FILE_NAME_PUBLIC = DOCUMENT_BASE + "/public/files/{name}";
        String DOCUMENT_FILE_NAME = DOCUMENT_BASE + "/files/{name}";
        String DOCUMENT_FILE_CODE = DOCUMENT_BASE + "/files/{code}";

        String DOCUMENT_REFERENCE = DOCUMENT_BASE + "/reference/{reference}";
        String GENERATE_DOCUMENT_REFERENCE = DOCUMENT_BASE + "/public/reference";
        String DOCUMENT_METRIC = DOCUMENT_BASE + "/metrics";
        String DOCUMENT_LICENSE_BY_NUMBER = DOCUMENT_BASE + "/license/public/{licenseNum}";
        String DOCUMENT_LICENSE_METRIC = DOCUMENT_BASE + "/license/metrics";
        String DOCUMENT_REPORT_METRIC = DOCUMENT_BASE + "/report/metrics";
        String DOCUMENT_STATUS = DOCUMENT_BASE + "/statues";
        String DOCUMENT_BY_STATUS = DOCUMENT_BASE + "/status/{status}";
        String DOCUMENT_REVIEW = DOCUMENT_BASE + "/REVIEW";
        String DOCUMENT_DECLINE = DOCUMENT_BASE + "/DECLINE";
        String DOCUMENT_APPROVE = DOCUMENT_BASE + "/APPROVE";
        String DOCUMENT_ISSUE = DOCUMENT_BASE + "/ISSUE";

        String DOCUMENT_FORM_TEMPLATE = DOCUMENT_BASE + "/template";
        String DOCUMENT_FORM_COMPONENT = DOCUMENT_BASE + "/components";

        String DOCUMENT_BASE_PUBLIC = DOCUMENT_BASE + "/public";
    }

    interface Payment {
        String PAYMENT_BASE = API_V1_URL + "/payments";
        String PAYMENT_CALLBACK = PAYMENT_BASE + "/callback";
        String CREATE_PAYMENT = PAYMENT_BASE + "/public";
        String GET_PAYMENT = PAYMENT_BASE + "/reference/{reference}";
        String GET_PAYMENT_EXTERNAL = PAYMENT_BASE + "/external/{externalReference}";
        String GET_PAYMENT_METRIC = PAYMENT_BASE + "/metrics";
        String GET_PAYMENT_STATUS = PAYMENT_BASE + "/status/{status}";
        String GET_PAYMENT_DATE_RANGE = PAYMENT_BASE + "/report/date-range";
    }

    interface Reporting {
        String REPORT_BASE = API_V1_URL + "/reports";
        String DATE_RANGE = REPORT_BASE + "/date-range";
        String APPLICATION_REPORT = DATE_RANGE + "/applications";
        String PAYMENT_REPORT = DATE_RANGE + "/payments";
        String APPLICATION_FILTER_REPORT = APPLICATION_REPORT + "/filter";
        String DATE_RANGE_PAYMENT = APPLICATION_REPORT + "/payment";
        String PAYMENT_FILTER_REPORT = PAYMENT_REPORT + "/status";
    }

    interface Authentication {
        String BASE_AUTH = API_V1_URL + "/auth/users";
        String AUTHORIZE_URL = BASE_AUTH + "/authorize/endpoint/{appId}";
        String AUTHORIZATION_CODE_URL = BASE_AUTH + "/token/endpoint/{appId}/{code}";
        String LOGOUT_URL = BASE_AUTH + "/logout/endpoint";
    }
}
