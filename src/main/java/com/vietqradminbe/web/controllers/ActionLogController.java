package com.vietqradminbe.web.controllers;

import com.vietqradminbe.application.services.ActionLogService;
import com.vietqradminbe.application.services.UserService;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.ErrorCode;
import com.vietqradminbe.domain.models.ActionLog;
import com.vietqradminbe.domain.models.User;
import com.vietqradminbe.infrastructure.configuration.security.utils.JwtUtil;
import com.vietqradminbe.infrastructure.configuration.timehelper.TimeHelperUtil;
import com.vietqradminbe.web.dto.response.APIResponse;
import com.vietqradminbe.web.dto.response.PagingDTO;
import com.vietqradminbe.web.dto.response.TransactionReceivePaginationResponseDTO;
import com.vietqradminbe.web.dto.response.interfaces.ActionLogListDTO;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class ActionLogController {
    static Logger logger = Logger.getLogger(UserController.class);

    UserService userService;
    JwtUtil jwtUtil;
    ActionLogService actionLogService;

    @GetMapping("/logs")
    public APIResponse<PagingDTO<ActionLogListDTO>> getLogs(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ) {
        APIResponse<PagingDTO<ActionLogListDTO>> response = new APIResponse<>();
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
                actionLog.setDescription("User :" + user.getUsername() + " " + user.getEmail() + " " + user.getFirstname() + " " + user.getLastname() + " " + user.getPhoneNumber() + " have just get all action logs at " + TimeHelperUtil.getCurrentTime());
                actionLogService.createActionLog(actionLog);
            }

            PagingDTO<ActionLogListDTO> actionLogs = actionLogService.getAllActionLogs(page, size);
            logger.info(ActionLogController.class + ": INFO: logs: " + actionLogs.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Get successfully!");
            response.setResult(actionLogs);
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(ErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            logger.error(ActionLogController.class + ": ERROR: logs: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(400);
            response.setMessage("E1005");
        }
        return response;
    }
}
