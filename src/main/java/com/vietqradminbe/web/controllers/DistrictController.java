package com.vietqradminbe.web.controllers;

import com.vietqradminbe.application.services.DistrictService;
import com.vietqradminbe.domain.models.District;
import com.vietqradminbe.web.dto.response.APIResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DistrictController {

    static Logger logger = Logger.getLogger(DistrictController.class.getName());
    DistrictService districtService;

    @GetMapping("districts")
    public ResponseEntity<APIResponse<List<District>>> getDistricts() {
        APIResponse<List<District>> response = new APIResponse<>();
        try {
            List<District> listDistricts =  districtService.getAllDistricts();
            logger.info(DistrictController.class + ": INFO: getDistricts: " + listDistricts.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Get successfully!");
            response.setResult(listDistricts);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error(DistrictController.class + ": ERROR: getDistricts: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(500);
            response.setMessage("E1005");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("districts/{provinceCode}")
    public ResponseEntity<APIResponse<List<District>>> getDistrictsByProvinceCode(@PathVariable int provinceCode) {
        APIResponse<List<District>> response = new APIResponse<>();
        try {
            List<District> listDistricts =  districtService.getAllDistrictsByProvinceId(provinceCode);
            logger.info(DistrictController.class + ": INFO: getDistricts: " + listDistricts.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Get successfully!");
            response.setResult(listDistricts);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error(DistrictController.class + ": ERROR: getDistricts: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(500);
            response.setMessage("E1005");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
