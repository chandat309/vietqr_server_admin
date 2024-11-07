package com.vietqradminbe.web.controllers;


import com.vietqradminbe.application.services.WardService;
import com.vietqradminbe.domain.models.District;
import com.vietqradminbe.domain.models.Ward;
import com.vietqradminbe.domain.repositories.WardRepository;
import com.vietqradminbe.web.dto.response.APIResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WardController {
    static Logger logger = Logger.getLogger(WardController.class.getName());
    WardService wardService;

    @GetMapping("wards")
    public ResponseEntity<APIResponse<List<Ward>>> getWards() {
        APIResponse<List<Ward>> response = new APIResponse<>();
        try {
            List<Ward> listWards =  wardService.getAllWards();
            logger.info(WardController.class + ": INFO: getWards: " + listWards.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Get successfully!");
            response.setResult(listWards);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error(WardController.class + ": ERROR: getWards: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(500);
            response.setMessage("E1005");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("wards/{districtCode}")
    public ResponseEntity<APIResponse<List<Ward>>> getWardsByDistrictCode(@PathVariable int districtCode) {
        APIResponse<List<Ward>> response = new APIResponse<>();
        try {
            List<Ward> listWards =  wardService.getAllWardsByDistrictId(districtCode);
            logger.info(WardController.class + ": INFO: getWards: " + listWards.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Get successfully!");
            response.setResult(listWards);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error(WardController.class + ": ERROR: getWards: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(500);
            response.setMessage("E1005");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
