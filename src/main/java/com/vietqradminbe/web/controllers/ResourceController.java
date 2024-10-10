package com.vietqradminbe.web.controllers;

import com.vietqradminbe.web.dto.response.APIResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resource")
public class ResourceController {

    @GetMapping("/secure")
    public APIResponse<String> secureResource(){
        APIResponse<String> response = new APIResponse<>();
        response.setCode(200);
        response.setMessage("Success");
        response.setResult("Yes, Your JWT Works...");
        return response;
    }
}
