package com.vietqradminbe.infrastructure.configuration.security;

import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.ErrorCode;
import com.vietqradminbe.infrastructure.configuration.security.utils.JwtAuthenticationFilter;
import com.vietqradminbe.infrastructure.configuration.security.utils.JwtAuthorizationFilter;
import com.vietqradminbe.infrastructure.configuration.security.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/register", "/api/v1/login/**", "/swagger-ui/**", "/swagger-ui.html"
            , "/v3/api-docs/**", "/h2-console/**", "/api/v1/version/**", "/api/v1/refreshtoken/**", "/api/v1/admin-keys-export/**", "/api/v1/provinces", "/api/v1/districts"
            , "/api/v1/districts/*", "/api/v1/wards", "/api/v1/wards/*"};


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        try {
            httpSecurity
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(requests -> {
                        RequestMatcher[] whiteListMatchers = Arrays.stream(WHITE_LIST_URL)
                                .map(AntPathRequestMatcher::new)
                                .toArray(RequestMatcher[]::new);

                        requests.requestMatchers(whiteListMatchers).permitAll()
                                // end point for users
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/users")).hasAuthority("ADMIN_ROLE")

                                // end point for roles
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/roles")).hasAuthority("ADMIN_ROLE")

                                // end point for functions (feature)
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/functions")).hasAuthority("ADMIN_ROLE")
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/functions/*")).hasAuthority("ADMIN_ROLE")

                                // end point for group function (feature)
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/group-functions")).hasAuthority("ADMIN_ROLE")
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/group-functions/*")).hasAuthority("ADMIN_ROLE")

                                // end point for transactions
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/transactions")).hasAuthority("ADMIN_ROLE")
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/transactions/*")).hasAuthority("ADMIN_ROLE")
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/transactions/v2")).hasAuthority("ADMIN_ROLE")
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/transactions/v3")).hasAuthority("ADMIN_ROLE")
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/transactions-refund")).hasAuthority("ADMIN_ROLE")
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/transaction-logs")).hasAuthority("ADMIN_ROLE")

                                // end point for generate key
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/key-active-bank/generate-key")).hasAuthority("ADMIN_ROLE")

                                // end point for activity user logs
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/logs")).hasAuthority("ADMIN_ROLE")
                                // end point for user
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/auth/reset-password")).hasAnyAuthority("ADMIN_ROLE", "ACCOUNTANCE_ROLE", "IT_SUPPORT_ROLE")
                                .anyRequest()
                                .authenticated();
                    })
                    .headers(headers -> headers
                            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)  // Allow frames from the same origin
                    )
                    .addFilter(new JwtAuthenticationFilter(authenticationManager(httpSecurity.getSharedObject(AuthenticationConfiguration.class)), jwtUtil))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager(httpSecurity.getSharedObject(AuthenticationConfiguration.class)), jwtUtil, customUserDetailsService));
            return httpSecurity.build();
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(ErrorCode.TOKEN_EXPIRED);
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        try {
            return authenticationConfiguration.getAuthenticationManager();
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(ErrorCode.TOKEN_EXPIRED);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); // Allow all origins
        configuration.addAllowedHeader("*");        // Allow all headers
        configuration.addAllowedMethod("*");        // Allow all HTTP methods
        configuration.setAllowCredentials(true);    // Allow credentials (optional)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all routes
        return source;
    }
}
