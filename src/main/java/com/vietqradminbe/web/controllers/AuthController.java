package com.vietqradminbe.web.controllers;


import com.vietqradminbe.application.services.ActionLogService;
import com.vietqradminbe.application.services.RefreshTokenService;
import com.vietqradminbe.application.services.RoleService;
import com.vietqradminbe.application.services.UserService;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.NotFoundException;
import com.vietqradminbe.domain.models.ActionLog;
import com.vietqradminbe.domain.models.RefreshToken;
import com.vietqradminbe.domain.models.User;
import com.vietqradminbe.infrastructure.configuration.security.utils.JwtUtil;
import com.vietqradminbe.infrastructure.configuration.timehelper.TimeHelperUtil;
import com.vietqradminbe.web.dto.request.AuthenticationRequest;
import com.vietqradminbe.web.dto.request.RefreshTokenRequest;
import com.vietqradminbe.web.dto.request.ResetPasswordRequest;
import com.vietqradminbe.web.dto.request.UserCreationRequest;
import com.vietqradminbe.web.dto.response.APIResponse;
import com.vietqradminbe.web.dto.response.RefreshTokenResponse;
import com.vietqradminbe.web.dto.response.UserLoginResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;


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
    ActionLogService actionLogService;
    static Logger logger = Logger.getLogger(AuthController.class.getName());

    @PostMapping("/login")
    public APIResponse<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException {
        APIResponse<UserLoginResponse> response = new APIResponse<>();
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
            userLoginResponse.setFirstName(user.getFirstname());
            userLoginResponse.setLastName(user.getLastname());
            userLoginResponse.setPhoneNumber(user.getPhoneNumber());


            response.setCode(200);
            response.setMessage("Success");
            response.setResult(userLoginResponse);
        } catch (BadCredentialsException badCredentialsException) {
            response.setCode(400);
            response.setMessage("E1002");
        }
        return response;
    }

    @PostMapping("/refreshtoken")
    public APIResponse<RefreshTokenResponse> refreshtoken(@Valid @RequestBody RefreshTokenRequest request) {

        APIResponse<RefreshTokenResponse> response = new APIResponse<>();

        String requestRefreshToken = request.getRefreshToken().trim();
        User userExisted = userService.getUserByRefreshToken(requestRefreshToken);
        if (userExisted == null) {
            response.setCode(404);
            response.setMessage("E1003");
            return response;
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userExisted.getUsername());

        String token = jwtUtil.generateToken(userDetails);
        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse();
        refreshTokenResponse.setRefreshToken(request.getRefreshToken().trim());
        refreshTokenResponse.setTokenType("Bearer ");
        refreshTokenResponse.setAccessToken(token);


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
    @Transactional
    public APIResponse<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        APIResponse<String> response = new APIResponse<>();
        try {
            HttpServletRequest currentRequest = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();

            // Extract Bearer token from the Authorization header
            String authorizationHeader = currentRequest.getHeader("Authorization");
            String token = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);  // Remove "Bearer " prefix

                //lay username tu token va lay user tu username
                String username = jwtUtil.extractUsernameFromToken(token.replace("Bearer ", ""));
                User user = userService.getUserByUsername(username);

                ActionLog actionLog = new ActionLog();
                actionLog.setUsername(username);
                actionLog.setId(UUID.randomUUID().toString());
                actionLog.setEmail(user.getEmail());
                actionLog.setFirstname(user.getFirstname());
                actionLog.setLastname(user.getLastname());
                actionLog.setPhoneNumber(user.getPhoneNumber());
                actionLog.setCreateAt(TimeHelperUtil.getCurrentTime());
                actionLog.setUpdateAt("");
                actionLog.setUser(user);
                actionLog.setDescription("User :" + user.getUsername() + " " + user.getEmail() + " " + user.getFirstname() + " " + user.getLastname() + " " + user.getPhoneNumber() + " have just reset password at " + TimeHelperUtil.getCurrentTime());
                actionLogService.createActionLog(actionLog);
            }


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
        } catch (Exception e) {
            logger.error(AuthController.class + ": ERROR: resetPassword: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(400);
            response.setMessage("E1005");
            response.setResult("FAILED");
        }
        return response;
    }
}
