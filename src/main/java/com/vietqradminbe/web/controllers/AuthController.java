package com.vietqradminbe.web.controllers;


import com.vietqradminbe.application.services.ActivityUserLogService;
import com.vietqradminbe.application.services.RefreshTokenService;
import com.vietqradminbe.application.services.RoleService;
import com.vietqradminbe.application.services.UserService;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.NotFoundException;
import com.vietqradminbe.domain.models.ActivityUserLog;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ActivityUserLogService activityUserLogService;
    static Logger logger = Logger.getLogger(AuthController.class.getName());

    @PostMapping("/login")
    public ResponseEntity<APIResponse<?>> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException {
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
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (BadCredentialsException badCredentialsException) {
            response.setCode(400);
            response.setMessage("E1002");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<APIResponse<RefreshTokenResponse>> refreshtoken(@Valid @RequestBody RefreshTokenRequest request) {

        APIResponse<RefreshTokenResponse> response = new APIResponse<>();

        String requestRefreshToken = request.getRefreshToken().trim();
        User userExisted = userService.getUserByRefreshToken(requestRefreshToken);
        if (userExisted == null) {
            response.setCode(404);
            response.setMessage("E1003");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<APIResponse<String>> createUser(@RequestBody @Valid UserCreationRequest request) {
        APIResponse<String> response = new APIResponse<>();
        try {
            User user = userService.createUserRequest(request);
            logger.info(AuthController.class + ": INFO: createUser: " + request.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Create successfully!");
            response.setResult("SUCCESS");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (BadRequestException e) {
            logger.error(AuthController.class + ": ERROR: createUser: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(400);
            response.setMessage("E1001");
            response.setResult("FAILED");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error(AuthController.class + ": ERROR: createUser: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(500);
            response.setMessage("E1005");
            response.setResult("FAILED");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/auth/reset-password")
    @Transactional
    public ResponseEntity<APIResponse<String>> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
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

                ActivityUserLog activityUserLog = new ActivityUserLog();
                activityUserLog.setUsername(username);
                activityUserLog.setId(UUID.randomUUID().toString());
                activityUserLog.setEmail(user.getEmail());
                activityUserLog.setFirstname(user.getFirstname());
                activityUserLog.setLastname(user.getLastname());
                activityUserLog.setPhoneNumber(user.getPhoneNumber());
                activityUserLog.setTimeLog(TimeHelperUtil.getCurrentTime());
                activityUserLog.setUser(user);
                activityUserLog.setActionJson("");
                activityUserLog.setGroupFunctionId("7303b0ff-adc5-4e34-aa8a-c019760e3522");
                activityUserLog.setFunctionId("f11f9ab4-4877-4069-8851-11e11d9ed53b");
                activityUserLog.setDescription("User :" + user.getUsername() + " " + user.getEmail() + " " + user.getFirstname() + " " + user.getLastname() + " " + user.getPhoneNumber() + " have just reset password at " + TimeHelperUtil.getCurrentTime());
                activityUserLogService.createActivityUserLog(activityUserLog);
            }


            userService.resetPasswordForUser(request);
            logger.info(AuthController.class + ": INFO: resetPassword: " + request.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Reset successfully!");
            response.setResult("SUCCESS");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (BadRequestException e) {
            logger.error(AuthController.class + ": ERROR: resetPassword: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(400);
            response.setMessage("E1007");
            response.setResult("FAILED");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (NotFoundException e) {
            logger.error(AuthController.class + ": ERROR: resetPassword: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(404);
            response.setMessage("E1003");
            response.setResult("FAILED");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            logger.error(AuthController.class + ": ERROR: resetPassword: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(500);
            response.setMessage("E1005");
            response.setResult("FAILED");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
