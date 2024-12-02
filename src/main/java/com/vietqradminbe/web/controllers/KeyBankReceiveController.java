package com.vietqradminbe.web.controllers;

import com.vietqradminbe.application.services.interfaces.IKeyActiveBankReceiveService;
import com.vietqradminbe.application.services.interfaces.IUserService;
import com.vietqradminbe.domain.models.User;
import com.vietqradminbe.web.dto.request.GenerateKeyBankDTO;
import com.vietqradminbe.web.dto.response.APIResponse;
import com.vietqradminbe.web.dto.response.PagingDTO;
import com.vietqradminbe.web.dto.response.interfaces.KeyActiveBankReceiveDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class KeyBankReceiveController {
    IUserService userService;
    IKeyActiveBankReceiveService keyActiveBankReceiveService;

    @GetMapping("/active-keys")
    public ResponseEntity<APIResponse<PagingDTO<KeyActiveBankReceiveDTO>>> getKeyActiveBankReceives(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        APIResponse<PagingDTO<KeyActiveBankReceiveDTO>> response = new APIResponse<>();
        try {
            PagingDTO<KeyActiveBankReceiveDTO> keys = keyActiveBankReceiveService.getKeyActiveBankReceives(startDate, endDate, page, size);

            response.setCode(200);
            response.setMessage("Keys retrieved successfully!");
            response.setResult(keys);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.setCode(500);
            response.setMessage("Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("key-active-bank/generate-key")
    public ResponseEntity<APIResponse<List<String>>> generateKeyForAdmin(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody GenerateKeyBankDTO dto
    ) {
        APIResponse<List<String>> response;
        HttpStatus httpStatus;
        try {
            String username = getUsernameFromToken(token);
            User user = userService.getUserByUsername(username);

            if (user != null && user.getUserRoles().stream()
                    .anyMatch(role -> "ADMIN_ROLE".equals(role.getRole().getRoleName()))) {

                log.info("generateKeyForAdmin: request: " + dto + " by: " + username + " at: " + System.currentTimeMillis());

                // Gọi service để generate và save keys
                List<String> keyActives = keyActiveBankReceiveService.generateAndSaveKeys(user, dto);

                response = new APIResponse<>(200, "Keys generated successfully", keyActives);
                httpStatus = HttpStatus.OK;
            } else {
                log.error("generateKeyForAdmin: Unauthorized role: token: " + token + " at: " + System.currentTimeMillis());
                response = new APIResponse<>(403, "Unauthorized - Only ADMIN_ROLE can perform this action", null);
                httpStatus = HttpStatus.FORBIDDEN;
            }

        } catch (Exception e) {
            log.error("generateKeyForAdmin: ERROR: " + e.getMessage() + " at " + System.currentTimeMillis());
            response = new APIResponse<>(400, "Internal error - Unable to generate keys", null);
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping("/admin-keys-export")
    public ResponseEntity<byte[]> exportKeysToExcel(@RequestParam List<String> keys) {
        List<KeyActiveBankReceiveDTO> keyDetails = keyActiveBankReceiveService.getKeyDetailsByKeys(keys);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Generated Keys");

            // Tạo CellStyle cho tiêu đề
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);  // In đậm
            headerFont.setFontHeightInPoints((short) 12); // Tăng kích thước chữ
            headerStyle.setFont(headerFont);

            // Tạo hàng tiêu đề với định dạng mới
            Row headerRow = sheet.createRow(0);
            String[] headers = {"STT", "Mã kích hoạt", "QR kích hoạt", "Thời gian hiệu lực", "Khởi tạo", "Trạng thái"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle); // Áp dụng style cho tiêu đề
            }

            // Điền dữ liệu vào các hàng
            for (int i = 0; i < keyDetails.size(); i++) {
                KeyActiveBankReceiveDTO keyDetail = keyDetails.get(i);
                Row row = sheet.createRow(i + 1);

                String qrLink = "https://vietqr.vn/service-active?key=" + keyDetail.getKeyActive();
                String duration = keyDetail.getDuration() + " tháng";
                String createAt = keyDetail.getCreateAt();
                String status = keyDetail.getStatus() == 1 ? "Đã kích hoạt" : "Chưa kích hoạt";

                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(keyDetail.getKeyActive());
                row.createCell(2).setCellValue(qrLink);
                row.createCell(3).setCellValue(duration);
                row.createCell(4).setCellValue(createAt);
                row.createCell(5).setCellValue(status);
            }

            // Thiết lập kích thước cột tự động
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Ghi workbook vào một mảng byte
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            // Thiết lập tiêu đề response
            HttpHeaders headersHttp = new HttpHeaders();
            headersHttp.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headersHttp.setContentDispositionFormData("attachment", "GeneratedKeys.xlsx");

            return new ResponseEntity<>(outputStream.toByteArray(), headersHttp, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public String getUsernameFromToken(String token) {
        String result = "";
        try {
            if (token != null && !token.trim().isEmpty()) {
                String secretKey = "A0B1C2D3E4F5061728394A5B6C7D8E9F1011121314151617181920212223242526272829303132333435363738393A3B3C3D3E3F40414243444546474849";
                String jwtToken = token.substring(7); // Bỏ đi tiền tố "Bearer "

                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey.getBytes())
                        .parseClaimsJws(jwtToken)
                        .getBody();

                // Lấy giá trị "username" từ token hoặc "sub" nếu username không tồn tại
                result = claims.get("username", String.class);
                if (result == null) {
                    result = claims.getSubject();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Để kiểm tra lỗi nếu có
        }
        return result;
    }

}

