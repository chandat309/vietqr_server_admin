package com.vietqradminbe.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest implements Serializable {
    private String id;
    private String oldPassword;
    private String newPassword;
}
