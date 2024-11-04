package com.vietqradminbe.web.controllers;

import com.vietqradminbe.application.services.ActivityUserLogService;
import com.vietqradminbe.application.services.RoleService;
import com.vietqradminbe.application.services.UserService;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.models.ActivityUserLog;
import com.vietqradminbe.domain.models.User;
import com.vietqradminbe.infrastructure.configuration.security.utils.JwtUtil;
import com.vietqradminbe.infrastructure.configuration.timehelper.TimeHelperUtil;
import com.vietqradminbe.web.dto.request.RoleCreationRequest;
import com.vietqradminbe.web.dto.response.APIResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    static Logger logger = Logger.getLogger(RoleController.class.getName());
    RoleService roleService;
    UserService userService;
    private final JwtUtil jwtUtil;
    private final ActivityUserLogService activityUserLogService;

    @PostMapping("roles")
    @Transactional
    public ResponseEntity<APIResponse<String>> createRole(@RequestBody @Valid RoleCreationRequest request) {
        APIResponse<String> response = new APIResponse<>();
        try {
            // Get the current HttpServletRequest
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
                activityUserLog.setDescription("User :" + user.getUsername() + " " + user.getEmail() + " " + user.getFirstname() + " " + user.getLastname() + " " + user.getPhoneNumber() + " have just create role at " + TimeHelperUtil.getCurrentTime());
                activityUserLogService.createActivityUserLog(activityUserLog);
            }

            roleService.createRoleRequest(request);
            logger.info(RoleController.class + ": INFO: createRole: " + request.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Create successfully!");
            response.setResult("SUCCESS");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (BadRequestException e) {
            logger.error(RoleController.class + ": ERROR: createRole: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(400);
            response.setMessage("E1009");
            response.setResult("FAILED");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error(RoleController.class + ": ERROR: createRole: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(500);
            response.setMessage("E1005");
            response.setResult("FAILED");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
