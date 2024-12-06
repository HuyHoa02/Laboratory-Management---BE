package com.chris.LaboratoryManagement.exception;

import com.chris.LaboratoryManagement.dto.response.ApiResponse;
import com.chris.LaboratoryManagement.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException exception){
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNCATEGORIZE_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZE_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception){
        ErrorCode errorCode = exception.getErrorCode();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        // If the error is due to token expiration, return HTTP 401
        if (errorCode == ErrorCode.TOKEN_EXPIRED) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        }

        // For other AppExceptions, return the normal 400 Bad Request
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
