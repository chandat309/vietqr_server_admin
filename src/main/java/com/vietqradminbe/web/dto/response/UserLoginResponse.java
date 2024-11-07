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
    String firstName;
    String lastName;
    String phoneNumber;
    List<String> roles;
    int isActive;
    String tokenType;
    String accessToken;
    String refreshToken;
}
