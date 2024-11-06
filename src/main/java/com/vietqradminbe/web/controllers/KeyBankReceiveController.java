package com.vietqradminbe.web.controllers;

import com.vietqradminbe.application.services.UserService;
import com.vietqradminbe.application.services.interfaces.IKeyActiveBankReceiveService;
import com.vietqradminbe.application.services.interfaces.IUserService;
import com.vietqradminbe.domain.models.KeyActiveBankReceive;
import com.vietqradminbe.domain.models.User;
import com.vietqradminbe.infrastructure.configuration.timehelper.TimeHelperUtil;
import com.vietqradminbe.infrastructure.configuration.utils.BcryptKeyUtil;
import com.vietqradminbe.infrastructure.configuration.utils.EnvironmentUtil;
import com.vietqradminbe.web.dto.request.GenerateKeyBankDTO;
import com.vietqradminbe.web.dto.response.APIResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class KeyBankReceiveController {
    IUserService userService;
    IKeyActiveBankReceiveService keyActiveBankReceiveService;

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
                List<String> keyActives = generateKeyActiveWithCheckDuplicated(dto.getNumOfKeys());
                List<KeyActiveBankReceive> entities = new ArrayList<>();

                for (String keyActive : keyActives) {
                    String secretKey = generateSecretKey(); // Khởi tạo secretKey trước
                    String valueActive = generateValueActive(keyActive, secretKey, dto.getDuration()); // Gọi generateValueActive với giá trị secretKey

                    KeyActiveBankReceive entity = KeyActiveBankReceive.builder()
                            .id(UUID.randomUUID().toString())
                            .keyActive(keyActive)
                            .secretKey(secretKey)
                            .valueActive(valueActive)
                            .duration(dto.getDuration())
                            .createAt(TimeHelperUtil.getCurrentTime())
                            .createBy(username)
                            .status(1)
                            .user(user)
                            .build();
                    entities.add(entity);
                }

                keyActiveBankReceiveService.insertAll(entities);
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

    private String generateSecretKey() {
        return UUID.randomUUID().toString();
    }

    private String generateValueActive(String keyActive, String secretKey, int duration) {
        return BcryptKeyUtil.hashKeyActive(keyActive, secretKey, duration);
    }

    private List<String> generateKeyActiveWithCheckDuplicated(int numOfKeys) {
        List<String> keys = new ArrayList<>();
        keys = generateMultikeyActive(numOfKeys);
        // check duplicated;
        List<String> keysDuplicated = new ArrayList<>();
        do {
            keysDuplicated = keyActiveBankReceiveService.checkDuplicatedKeyActives(keys);
            if (keysDuplicated.isEmpty()) {
                break;
            }
            // remove duplicated
            for (String key : keysDuplicated) {
                keys.remove(key);
            }
            // generate new key
            List<String> newKeys = generateMultikeyActive(keysDuplicated.size());
            keys.addAll(newKeys);
        } while (!keysDuplicated.isEmpty());
        return keys;
    }

    private List<String> generateMultikeyActive(int numOfKeys) {
        Set<String> keys = new HashSet<>();
        for (int i = 0; i < numOfKeys; i++) {
            String key = generateKeyActive();
            keys.add(key);
        }
        return new ArrayList<>(keys);
    }

    private String generateKeyActive() {
        Random random = new Random();
        int length = EnvironmentUtil.getLengthKeyActiveBank();
        String characters = EnvironmentUtil.getCharactersKeyActiveBank();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    private boolean isKeyActive(String keyActive, String secretKey, int duration, String valueActive) {
        String data = BcryptKeyUtil.hashKeyActive(keyActive, secretKey, duration);
        return data.equals(valueActive);
    }


}

