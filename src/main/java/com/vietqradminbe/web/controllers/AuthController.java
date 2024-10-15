package com.vietqradminbe.web.controllers;


import com.vietqradminbe.application.services.RefreshTokenService;
import com.vietqradminbe.application.services.RoleService;
import com.vietqradminbe.application.services.UserService;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.ErrorCode;
import com.vietqradminbe.domain.exceptions.NotFoundException;
import com.vietqradminbe.domain.models.RefreshToken;
import com.vietqradminbe.domain.models.User;
import com.vietqradminbe.domain.repositories.RoleRepository;
import com.vietqradminbe.infrastructure.configuration.security.utils.JwtUtil;
import com.vietqradminbe.infrastructure.configuration.timehelper.TimeHelperUtil;
import com.vietqradminbe.web.dto.request.AuthenticationRequest;
import com.vietqradminbe.web.dto.request.RefreshTokenRequest;
import com.vietqradminbe.web.dto.request.ResetPasswordRequest;
import com.vietqradminbe.web.dto.request.UserCreationRequest;
import com.vietqradminbe.web.dto.response.APIResponse;
import com.vietqradminbe.web.dto.response.RefreshTokenResponse;
import com.vietqradminbe.web.dto.response.UserLoginResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthenticationManager authenticationManager;
    JwtUtil jwtUtil;
    UserDetailsService userDetailsService;
    UserService userService;
    RefreshTokenService refreshTokenService;
    RoleService roleService;
    static Logger logger = Logger.getLogger(AuthController.class.getName());
    private final RoleRepository roleRepository;

    @PostMapping("/login")
    public APIResponse<UserLoginResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername().trim(), authenticationRequest.getPassword().trim()));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername().trim());

            final String jwt = jwtUtil.generateToken(userDetails);

            //generate refresh token

            User user = userService.getUserByUsername(userDetails.getUsername());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());


            //set response
            List<String> roles = roleService.getRolesNameByUsername(user.getUsername());
            UserLoginResponse userLoginResponse = new UserLoginResponse();
            userLoginResponse.setUsername(user.getUsername());
            userLoginResponse.setRefreshToken(refreshToken.getToken());
            userLoginResponse.setId(user.getId());
            userLoginResponse.setEmail(user.getEmail());
            userLoginResponse.setIsActive(user.getIsActive());
            userLoginResponse.setTokenType("Bearer ");
            userLoginResponse.setAccessToken(jwt);
            userLoginResponse.setRoles(roles);

            APIResponse<UserLoginResponse> response = new APIResponse<>();
            response.setCode(200);
            response.setMessage("Success");
            response.setResult(userLoginResponse);
            return response;
        } catch (BadCredentialsException badCredentialsException) {
            logger.error("Incorrect username or password");
            throw new BadRequestException(ErrorCode.INVALID_USERNAME_OR_PASSWORD);
        }
    }

    @PostMapping("/refreshtoken")
    public APIResponse<RefreshTokenResponse> refreshtoken(@Valid @RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken().trim();
        User userExisted = userService.getUserByRefreshToken(requestRefreshToken);
        if (userExisted == null) {
            throw new BadRequestException(ErrorCode.USER_NOTFOUND);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userExisted.getUsername());

        String token = jwtUtil.generateToken(userDetails);
        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse();
        refreshTokenResponse.setRefreshToken(request.getRefreshToken().trim());
        refreshTokenResponse.setTokenType("Bearer ");
        refreshTokenResponse.setAccessToken(token);


        APIResponse<RefreshTokenResponse> response = new APIResponse<>();
        response.setCode(200);
        response.setMessage("Success");
        response.setResult(refreshTokenResponse);
        return response;
    }

    @PostMapping("/auth/register")
    public APIResponse<String> createUser(@RequestBody @Valid UserCreationRequest request) {
        APIResponse<String> response = new APIResponse<>();
        try {
            User user = userService.createUserRequest(request);
            logger.info(AuthController.class + ": INFO: createUser: " + request.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Create successfully!");
            response.setResult("SUCCESS");
        } catch (BadRequestException e) {
            logger.error(AuthController.class + ": ERROR: createUser: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(400);
            response.setMessage("E1001");
            response.setResult("FAILED");
        } catch (Exception e) {
            logger.error(AuthController.class + ": ERROR: createUser: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(400);
            response.setMessage("E1005");
            response.setResult("FAILED");
        }
        return response;
    }

    @PostMapping("/auth/reset-password")
    public APIResponse<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        APIResponse<String> response = new APIResponse<>();
        try {
            userService.resetPasswordForUser(request);
            logger.info(AuthController.class + ": INFO: resetPassword: " + request.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Reset successfully!");
            response.setResult("SUCCESS");
        } catch (BadRequestException e) {
            logger.error(AuthController.class + ": ERROR: resetPassword: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(400);
            response.setMessage("E1006");
            response.setResult("FAILED");
        } catch (NotFoundException e) {
            logger.error(AuthController.class + ": ERROR: resetPassword: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(404);
            response.setMessage("E1003");
            response.setResult("FAILED");
        }catch (Exception e) {
            logger.error(AuthController.class + ": ERROR: resetPassword: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(400);
            response.setMessage("E1005");
            response.setResult("FAILED");
        }
        return response;
    }
}
