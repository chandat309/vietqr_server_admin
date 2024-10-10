package com.vietqradminbe.domain.exceptions;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)

public enum ErrorCode {
    USER_EXISTED(400,"User existed!"),
    ACCOUNT_EXISTED(400,"Account existed!"),
    USER_NOTFOUND(404,"User not found!"),
    ADMIN_NOTFOUND(404,"Admin not found!"),
    INVALID_KEY(400,"Invalid message key!"),
    USERNAME_INVALID(400,"Username must at least 3 characters!"),
    PASSWORD_INVALID(400,"Password must at least 8 characters!"),
    PASSWORD_NULL(400,"Password cannot be null!"),
    INVALID_USERNAME_OR_PASSWORD(400,"Invalid username or password!"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    final int code;
    final String message;
}
