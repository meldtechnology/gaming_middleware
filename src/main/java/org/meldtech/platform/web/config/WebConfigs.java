package org.meldtech.platform.web.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.meldtech.platform.model.api.request.PasswordRestRecord;
import org.meldtech.platform.model.api.request.UserRecord;
import org.meldtech.platform.model.api.response.FullUserProfileRecord;
import org.meldtech.platform.model.dto.UploadFileRequest;
import org.meldtech.platform.web.authorizer.AuthorizerHandler;
import org.meldtech.platform.model.dto.OAuth2RegisteredClientRecord;
import org.meldtech.platform.web.roles.RoleHandler;
import org.meldtech.platform.web.users.UserProfileHandler;
import org.meldtech.platform.web.users.UserSignInHandler;
import org.meldtech.platform.web.users.UserSignUpHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.meldtech.platform.web.config.AppRoutes.Authentication.*;
import static org.meldtech.platform.web.config.AppRoutes.Client.CLIENT_BASE;
import static org.meldtech.platform.web.config.AppRoutes.Client.CLIENT_BY_ID;
import static org.meldtech.platform.web.config.AppRoutes.Document.DOCUMENT_UPLOAD;
import static org.meldtech.platform.web.config.AppRoutes.Role.ROLE_BASE;
import static org.meldtech.platform.web.config.AppRoutes.User.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Tag(name="OAuth2 Client Setting API", description = "OAuth2 Client API")
public class WebConfigs {

    @Bean
    @RouterOperations({
            @RouterOperation(path = CLIENT_BY_ID, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET, beanClass = AuthorizerHandler.class, beanMethod = "getClientById",
                    operation = @Operation(operationId = "getClientById", tags = "OAuth2 Registered Clients API",
                            description = "Get Client by Id", summary = "Get Client by Id",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "clientId")})
            ),
            @RouterOperation(path = CLIENT_BASE, produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET, beanClass = AuthorizerHandler.class, beanMethod = "getAllClient",
                    operation = @Operation( operationId = "getAllClient", tags = "OAuth2 Registered Clients API",
                            description = "Get All Clients", summary = "Get All Clients",
                             parameters = {
                            @Parameter(in = ParameterIn.QUERY, name = "page", required = true, example = "1"),
                            @Parameter(in = ParameterIn.QUERY, name = "size", required = true, example = "10"),
                            @Parameter(in = ParameterIn.QUERY, name = "sortBy", required = true, example = "clientIdIssuedAt"),
                            @Parameter(in = ParameterIn.QUERY, name = "sortIn", example = "ASC"),
                            @Parameter(in = ParameterIn.QUERY, name = "start"),
                            @Parameter(in = ParameterIn.QUERY, name = "end"),
                            @Parameter(in = ParameterIn.QUERY, name = "search")
                    })
            ),
            @RouterOperation(path = CLIENT_BASE, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST, beanClass = AuthorizerHandler.class, beanMethod = "addClient",
                    operation = @Operation(operationId = "addClient", tags = "OAuth2 Registered Clients API",
                            description = "Add a New Client", summary = "Add a New Client",
                            requestBody = @RequestBody(content = @Content(schema =
                            @Schema( implementation = OAuth2RegisteredClientRecord.class)))
                    )
            ),
            @RouterOperation(path = CLIENT_BY_ID, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT, beanClass = AuthorizerHandler.class, beanMethod = "updateClient",
                    operation = @Operation(operationId = "updateClient", tags = "OAuth2 Registered Clients API",
                            description = "Edit Client", summary = "Edit Client",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "clientId")},
                            requestBody = @RequestBody(content = @Content(schema =
                            @Schema( implementation = OAuth2RegisteredClientRecord.class)))
                    )
            ),
            @RouterOperation(path = CLIENT_BY_ID, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.DELETE, beanClass = AuthorizerHandler.class, beanMethod = "deleteClient",
                    operation = @Operation(operationId = "deleteClient", tags = "OAuth2 Registered Clients API",
                            description = "Delete a Client", summary = "Delete a Client",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "clientId")}
                    )
            ),
    })
    public RouterFunction<ServerResponse> authorizerEndpointHandler(AuthorizerHandler handler) {
        return route()
                .GET(CLIENT_BASE, accept(MediaType.APPLICATION_JSON), handler::getAllClient)
                .GET(CLIENT_BY_ID, accept(MediaType.APPLICATION_JSON), handler::getClientById)
                .POST(CLIENT_BASE, accept(MediaType.APPLICATION_JSON)
                        .and(contentType(MediaType.APPLICATION_JSON)), handler::addClient)
                .PUT(CLIENT_BY_ID, accept(MediaType.APPLICATION_JSON)
                        .and(contentType(MediaType.APPLICATION_JSON)), handler::updateClient)
                .DELETE(CLIENT_BY_ID, accept(MediaType.APPLICATION_JSON), handler::deleteClient)
                .build();
    }

    // Application Roles

    @Bean
    @RouterOperations({
            @RouterOperation(path = ROLE_BASE, produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET, beanClass = RoleHandler.class, beanMethod = "getRoles",
                    operation = @Operation( operationId = "getRoles", tags = "Application Roles API",
                            description = "Get Application Roles", summary = "Get Application Roles",
                             parameters = {
                            @Parameter(in = ParameterIn.QUERY, name = "page", required = true, example = "1"),
                            @Parameter(in = ParameterIn.QUERY, name = "size", required = true, example = "10"),
                            @Parameter(in = ParameterIn.QUERY, name = "sortBy"),
                            @Parameter(in = ParameterIn.QUERY, name = "sortIn", example = "ASC"),
                            @Parameter(in = ParameterIn.QUERY, name = "start"),
                            @Parameter(in = ParameterIn.QUERY, name = "end"),
                            @Parameter(in = ParameterIn.QUERY, name = "search")
                    })
            ),
    })
    public RouterFunction<ServerResponse> rolesEndpointHandler(RoleHandler handler) {
        return route()
                .GET(ROLE_BASE, accept(MediaType.APPLICATION_JSON), handler::getRoles)
                .build();
    }

    // Application Users

    @Bean
    @RouterOperations({
            @RouterOperation(path = USER_PROFILE_ADMIN_BASE, produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET, beanClass = UserProfileHandler.class, beanMethod = "getProfiles",
                    operation = @Operation( operationId = "getProfiles", tags = "User Profiles API",
                            description = "Get Users Profiles", summary = "Get Users Profiles",
                             parameters = {
                            @Parameter(in = ParameterIn.QUERY, name = "page", required = true, example = "1"),
                            @Parameter(in = ParameterIn.QUERY, name = "size", required = true, example = "10"),
                            @Parameter(in = ParameterIn.QUERY, name = "sortBy"),
                            @Parameter(in = ParameterIn.QUERY, name = "sortIn", example = "ASC"),
                            @Parameter(in = ParameterIn.QUERY, name = "start"),
                            @Parameter(in = ParameterIn.QUERY, name = "end"),
                            @Parameter(in = ParameterIn.QUERY, name = "search")
                    })
            ),
            @RouterOperation(path = USER_PROFILE_BASE, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET, beanClass = UserProfileHandler.class, beanMethod = "getUserProfile",
                    operation = @Operation(operationId = "getUserProfile", tags = "User Profiles API",
                            description = "Get App User Profile", summary = "Get App User Profile")
            ),
            @RouterOperation(path = USER_PROFILE_BY_ID, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET, beanClass = UserProfileHandler.class, beanMethod = "getUserProfileByAdmin",
                    operation = @Operation(operationId = "getUserProfileByAdmin", tags = "User Profiles API",
                            description = "Get User Profile By Admin", summary = "Get User Profile By Admin",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "publicId")})
            ),
            @RouterOperation(path = USER_PROFILE_BASE, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT, beanClass = UserProfileHandler.class, beanMethod = "editUserProfile",
                    operation = @Operation(operationId = "editUserProfile", tags = "User Profiles API",
                            description = "Update User Profile Details", summary = "Update User Profile Details",
                            requestBody = @RequestBody(content = @Content(schema =
                            @Schema( implementation = FullUserProfileRecord.class)))
                    )
            ),
            @RouterOperation(path = USER_PROFILE_METRIC, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET, beanClass = UserProfileHandler.class, beanMethod = "getUserMetric",
                    operation = @Operation(operationId = "getUserMetric", tags = "User Profiles API",
                            description = "Get User Metrics", summary = "Get User Metrics"
                    )
            ),
    })
    public RouterFunction<ServerResponse> profileEndpointHandler(UserProfileHandler handler) {
        return route()
                .GET(USER_PROFILE_ADMIN_BASE, accept(MediaType.APPLICATION_JSON), handler::getProfiles)
                .GET(USER_PROFILE_BASE, accept(MediaType.APPLICATION_JSON), handler::getUserProfile)
                .GET(USER_PROFILE_BY_ID, accept(MediaType.APPLICATION_JSON), handler::getUserProfileByAdmin)
                .GET(USER_PROFILE_METRIC, accept(MediaType.APPLICATION_JSON), handler::getUserMetric)
                .PUT(USER_PROFILE_BASE, accept(MediaType.APPLICATION_JSON)
                        .and(contentType(MediaType.APPLICATION_JSON)), handler::editUserProfile)
                .build();
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = USER_RESEND_OTP, produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET, beanClass = UserSignUpHandler.class, beanMethod = "resendOtp",
                    operation = @Operation( operationId = "resendOtp", tags = "User Account API",
                            description = "Resend Otp", summary = "Resend Otp",
                             parameters = { @Parameter(in = ParameterIn.PATH, name = "email", required = true),
                                     @Parameter(in = ParameterIn.QUERY, name = "username"),
                    })
            ),
            @RouterOperation(path = USER_CHANGE_PASSWORD_PUBLIC_ID, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET, beanClass = UserSignUpHandler.class, beanMethod = "resetUserPassword",
                    operation = @Operation(operationId = "resetUserPassword", tags = "User Account API",
                            description = "User Reset Password (public)", summary = "User Reset Password (public)",
                            parameters = { @Parameter(in = ParameterIn.PATH, name = "email", required = true) })

            ),
            @RouterOperation(path = USER_SIGN_UP, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST, beanClass = UserSignUpHandler.class, beanMethod = "addNewUser",
                    operation = @Operation(operationId = "addNewUser", tags = "User Account API",
                            description = "Add a New User Account", summary = "Add a New User Account",
                            requestBody = @RequestBody(content = @Content(schema =
                            @Schema( implementation = UserRecord.class)))
                    )
            ),
            @RouterOperation(path = USER_CHANGE_PASSWORD, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT, beanClass = UserSignUpHandler.class, beanMethod = "changePassword",
                    operation = @Operation(operationId = "changePassword", tags = "User Account API",
                            description = "Change User Password", summary = "Change User Password",
                            requestBody = @RequestBody(content = @Content(schema =
                            @Schema( implementation = PasswordRestRecord.class)))
                    )
            ),
            @RouterOperation(path = USER_VERIFY_OTP, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT, beanClass = UserSignUpHandler.class, beanMethod = "verifyOtp",
                    operation = @Operation(operationId = "verifyOtp", tags = "User Account API",
                            description = "Verify One Time Password (OTP)", summary = "Verify One Time Password (OTP)",
                            parameters = { @Parameter(in = ParameterIn.PATH, name = "otp", required = true)}
                    )
            ),
            @RouterOperation(path = USER_DISABLE, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT, beanClass = UserSignUpHandler.class, beanMethod = "disableUser",
                    operation = @Operation(operationId = "disableUser", tags = "User Account API",
                            description = "De-Activate User", summary = "De-Activate User",
                            parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId", required = true)}
                    )
            ),
            @RouterOperation(path = DOCUMENT_UPLOAD, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST, beanClass = UserSignUpHandler.class, beanMethod = "uploadDocument",
                    operation = @Operation(operationId = "uploadDocument", tags = "Document Upload API",
                            description = "Upload Image", summary = "Upload Image",
                            requestBody = @RequestBody(content = @Content(schema =
                            @Schema( implementation = UploadFileRequest.class)))
                    )
            ),
    })
    public RouterFunction<ServerResponse> userEndpointHandler(UserSignUpHandler handler) {
        return route()
                .GET(USER_RESEND_OTP, accept(MediaType.APPLICATION_JSON), handler::resendOtp)
                .GET(USER_CHANGE_PASSWORD_PUBLIC_ID, accept(MediaType.APPLICATION_JSON), handler::resetUserPassword)
                .POST(USER_SIGN_UP, accept(MediaType.APPLICATION_JSON)
                        .and(contentType(MediaType.APPLICATION_JSON)), handler::addNewUser)
                .PUT(USER_CHANGE_PASSWORD, accept(MediaType.APPLICATION_JSON)
                        .and(contentType(MediaType.APPLICATION_JSON)), handler::changePassword).
                PUT(USER_VERIFY_OTP, accept(MediaType.APPLICATION_JSON), handler::verifyOtp)
                .PUT(USER_DISABLE, accept(MediaType.APPLICATION_JSON), handler::disableUser)
                .POST(DOCUMENT_UPLOAD, accept(MediaType.APPLICATION_JSON), handler::uploadDocument)
                .build();
    }


    @Bean
    @RouterOperations({
            @RouterOperation(path = AUTHORIZE_URL, produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET, beanClass = UserSignInHandler.class, beanMethod = "getAuthorizeEndpoint",
                    operation = @Operation( operationId = "getAuthorizeEndpoint", tags = "Authentication API",
                            description = "Request for Login Authorizer url", summary = "Request for Login Authorizer url"
                    )
            ),
            @RouterOperation(path = AUTHORIZATION_CODE_URL, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST, beanClass = UserSignInHandler.class, beanMethod = "requestAccessToken",
                    operation = @Operation(operationId = "requestAccessToken", tags = "Authentication API",
                            description = "Get Access Token", summary = "Get Access Token",
                            parameters = { @Parameter(in = ParameterIn.PATH, name = "code", required = true)}
                    )
            ),
            @RouterOperation(path = LOGOUT_URL, produces = { MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET, beanClass = UserSignInHandler.class, beanMethod = "logout",
                    operation = @Operation(operationId = "logout", tags = "Authentication API",
                            description = "Sign out from app", summary = "Sign out from app"
                    )
            ),
    })
    public RouterFunction<ServerResponse> authenticationEndpointHandler(UserSignInHandler handler) {
        return route()
                .GET(AUTHORIZE_URL, accept(MediaType.APPLICATION_JSON), handler::getAuthorizeEndpoint)
                .POST(AUTHORIZATION_CODE_URL, accept(MediaType.APPLICATION_JSON), handler::requestAccessToken)
                .GET(LOGOUT_URL, accept(MediaType.APPLICATION_JSON), handler::logout)
                .build();
    }

}
