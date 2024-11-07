package com.vietqradminbe.web.controllers;

import com.vietqradminbe.application.services.ProvinceService;
import com.vietqradminbe.domain.models.Province;
import com.vietqradminbe.web.dto.response.APIResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProvinceController {
    static Logger logger = Logger.getLogger(ProvinceController.class.getName());
    ProvinceService provinceService;

    @GetMapping("provinces")
    public ResponseEntity<APIResponse<List<Province>>> getProvinces() {
        APIResponse<List<Province>> response = new APIResponse<>();
        try {
            List<Province> listProvinces =  provinceService.getAllProvinces();
            logger.info(ProvinceController.class + ": INFO: getProvinces: " + listProvinces.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Get successfully!");
            response.setResult(listProvinces);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error(ProvinceController.class + ": ERROR: getProvinces: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(500);
            response.setMessage("E1005");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
