package com.vietqradminbe.web.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserLoginResponse {
    String id;
    String username;
    String email;
    List<String> roles;
    Boolean isActive;
    String tokenType;
    String accessToken;
    String refreshToken;
}
