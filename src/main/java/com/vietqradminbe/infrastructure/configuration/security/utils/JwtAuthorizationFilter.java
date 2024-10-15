package com.vietqradminbe.infrastructure.configuration.security.utils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.ErrorCode;
import com.vietqradminbe.web.controllers.RoleController;
import com.vietqradminbe.web.dto.response.APIResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Objects;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    static Logger log = Logger.getLogger(RoleController.class.getName());


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String header = request.getHeader("Authorization");
            if (Objects.isNull(header) || !header.startsWith("Bearer ")) {
                chain.doFilter(request, response);
                return;
            }
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            handleException(response, 400, ErrorCode.TOKEN_EXPIRED.getMessage());
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UsernamePasswordAuthenticationToken authentication = null;
        if (Objects.nonNull(token)) {
            String username = jwtUtil.extractUsernameFromToken(token.replace("Bearer ", ""));
            if (Objects.nonNull(username)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(token.replace("Bearer ", ""), userDetails)) {
                    authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                }
            }
        }
        return authentication;
    }

    private void handleException(HttpServletResponse response, int errorCode, String errorMessage) throws IOException {
        APIResponse<Object> apiResponse = new APIResponse<>();
        apiResponse.setCode(errorCode);
        apiResponse.setMessage(errorMessage);
        apiResponse.setResult(null); // No result in case of error

        // Set response status and headers
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Write the response as JSON
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(apiResponse));
    }
}
