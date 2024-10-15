package com.vietqradminbe.web.controllers;

import com.vietqradminbe.application.services.UserService;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.ErrorCode;
import com.vietqradminbe.domain.models.User;
import com.vietqradminbe.web.dto.response.APIResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    static Logger logger = Logger.getLogger(UserController.class);

    UserService userService;

    @GetMapping("/version")
    public String getJavaVersion() {
        return System.getProperty("java.version");
    }


    @GetMapping("/users")
    public APIResponse<List<User>> getUsers() {
        APIResponse<List<User>> response = new APIResponse<>();
        try {
            List<User> users = userService.getAllUsers();
            logger.info(UserController.class + ": INFO: getUsers: " + users.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Get successfully!");
            response.setResult(users);
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(ErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            logger.error(UserController.class + ": ERROR: getUser: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(400);
            response.setMessage("E1005");
        }
        return response;
    }
}
