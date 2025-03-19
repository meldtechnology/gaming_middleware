package org.meldtech.platform.config;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.config.helper.CombinedClaimConverter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * @Author: Josiah Adetayo
 * @Email: josleke@gmail.com, josiah.adetayo@meld-tech.com
 * @Date: 12/6/23
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class ResourceServer {
    private final CombinedClaimConverter converter;
    private final List<CorsConfiguration> corsConfigurations;
    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec
                                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                                .pathMatchers(AUTH_WHITELIST).permitAll()
                                .anyExchange()
                                .authenticated()
                )
                .oauth2ResourceServer(oAuth2 -> oAuth2.jwt(jwtSpec ->
                        jwtSpec.jwtAuthenticationConverter(converter)));
        return http.build();
    }

    private static final String[] AUTH_WHITELIST = {
            "/v1/oauth2/clients/**",
            "/v1/auth/users/**",
            "/v1/documents/upload/**",
            "/v1/documents/public/**",
            "/v1/users/verify/**",
            "/v1/users/public/**",
            "/v1/payments/callback/**",
            "/v1/payments/public/**",
            "/v1/documents/license/public/**",
//            "/v1/documents/**",
//            "/v1/**",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            // other public endpoints of your API may be appended to this array
    };
}
