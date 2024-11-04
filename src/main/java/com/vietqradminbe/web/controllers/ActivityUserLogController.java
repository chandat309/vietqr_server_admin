package com.vietqradminbe.web.controllers;

import com.vietqradminbe.application.services.ActivityUserLogService;
import com.vietqradminbe.application.services.UserService;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.ErrorCode;
import com.vietqradminbe.domain.models.ActivityUserLog;
import com.vietqradminbe.domain.models.User;
import com.vietqradminbe.infrastructure.configuration.security.utils.JwtUtil;
import com.vietqradminbe.infrastructure.configuration.timehelper.TimeHelperUtil;
import com.vietqradminbe.web.dto.response.APIResponse;
import com.vietqradminbe.web.dto.response.PagingDTO;
import com.vietqradminbe.web.dto.response.interfaces.ActivityUserLogListDTO;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActivityUserLogController {
    static Logger logger = Logger.getLogger(UserController.class);

    UserService userService;
    JwtUtil jwtUtil;
    ActivityUserLogService activityUserLogRepository;

    @GetMapping("/logs")
    public ResponseEntity<APIResponse<PagingDTO<ActivityUserLogListDTO>>> getLogs(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ) {
        APIResponse<PagingDTO<ActivityUserLogListDTO>> response = new APIResponse<>();
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
                activityUserLog.setDescription("User :" + user.getUsername() + " " + user.getEmail() + " " + user.getFirstname() + " " + user.getLastname() + " " + user.getPhoneNumber() + " have just get all action logs at " + TimeHelperUtil.getCurrentTime());
                activityUserLogRepository.createActivityUserLog(activityUserLog);
            }

            PagingDTO<ActivityUserLogListDTO> actionLogs = activityUserLogRepository.getAllActivityUserLogs(page, size);
            logger.info(ActivityUserLogController.class + ": INFO: logs: " + actionLogs.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Get successfully!");
            response.setResult(actionLogs);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(ErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            logger.error(ActivityUserLogController.class + ": ERROR: logs: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(500);
            response.setMessage("E1005");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
