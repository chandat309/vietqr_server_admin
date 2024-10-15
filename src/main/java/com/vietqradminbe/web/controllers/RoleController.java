package com.vietqradminbe.web.controllers;

import com.vietqradminbe.application.services.RoleService;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.models.User;
import com.vietqradminbe.web.dto.request.RoleCreationRequest;
import com.vietqradminbe.web.dto.request.UserCreationRequest;
import com.vietqradminbe.web.dto.response.APIResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    static Logger logger = Logger.getLogger(RoleController.class.getName());
    RoleService roleService;

    @PostMapping("roles")
    public APIResponse<String> createRole(@RequestBody @Valid RoleCreationRequest request) {
        APIResponse<String> response = new APIResponse<>();
        try {
            roleService.createRoleRequest(request);
            logger.info(RoleController.class + ": INFO: createRole: " + request.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Create successfully!");
            response.setResult("SUCCESS");
        } catch (BadRequestException e) {
            logger.error(RoleController.class + ": ERROR: createRole: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(400);
            response.setMessage("E1009");
            response.setResult("FAILED");
        } catch (Exception e) {
            logger.error(RoleController.class + ": ERROR: createRole: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(400);
            response.setMessage("E1005");
            response.setResult("FAILED");
        }
        return response;
    }
}
