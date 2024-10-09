package com.vietqradminbe.domain.exceptions;

import com.vietqradminbe.web.dto.response.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<APIResponse> handlingRuntimeException(RuntimeException e) {
        APIResponse apiResponse = new APIResponse();

        apiResponse.setMessage(e.getMessage());
        apiResponse.setCode(400);
        return ResponseEntity.badRequest().body(apiResponse);
    }


    @ExceptionHandler(value = BadRequestException.class)
    ResponseEntity<APIResponse> handlingBadRequestException(BadRequestException e) {
        ErrorCode errorResponse = e.getErrorCode();
        APIResponse apiResponse = new APIResponse();
        apiResponse.setMessage(errorResponse.getMessage());
        apiResponse.setCode(errorResponse.getCode());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = NotFoundException.class)
    ResponseEntity<APIResponse> handlingNotFoundException(NotFoundException e) {
        ErrorCode errorResponse = e.getErrorCode();
        APIResponse apiResponse = new APIResponse();
        apiResponse.setMessage(errorResponse.getMessage());
        apiResponse.setCode(errorResponse.getCode());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse> handlingValidation(MethodArgumentNotValidException e) {
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException exception) {

        }

        APIResponse apiResponse = new APIResponse();
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
