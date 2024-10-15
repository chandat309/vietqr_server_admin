package com.vietqradminbe.infrastructure.configuration.security;

import com.vietqradminbe.infrastructure.configuration.security.utils.JwtAuthenticationFilter;
import com.vietqradminbe.infrastructure.configuration.security.utils.JwtAuthorizationFilter;
import com.vietqradminbe.infrastructure.configuration.security.utils.JwtUtil;
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

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**", "/api/v1/login/**", "/swagger-ui/**", "/swagger-ui.html"
            , "/v3/api-docs/**", "/h2-console/**", "/api/v1/version/**", "/api/v1/refreshtoken/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> {
                    RequestMatcher[] whiteListMatchers = Arrays.stream(WHITE_LIST_URL)
                            .map(AntPathRequestMatcher::new)
                            .toArray(RequestMatcher[]::new);

                    requests.requestMatchers(whiteListMatchers).permitAll()
                            //authorize for admin role
                            .requestMatchers(new AntPathRequestMatcher("/api/v1/users")).hasAuthority("ADMIN_ROLE")
                            .anyRequest()
                            .authenticated();
                })
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)  // Allow frames from the same origin
                )
                .addFilter(new JwtAuthenticationFilter(authenticationManager(httpSecurity.getSharedObject(AuthenticationConfiguration.class)), jwtUtil))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(httpSecurity.getSharedObject(AuthenticationConfiguration.class)), jwtUtil, customUserDetailsService));
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
