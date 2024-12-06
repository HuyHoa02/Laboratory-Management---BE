package com.chris.LaboratoryManagement.configuration;


import ch.qos.logback.core.spi.ErrorCodes;
import com.chris.LaboratoryManagement.dto.response.ResponseError;
import com.chris.LaboratoryManagement.enums.ErrorCode;
import com.chris.LaboratoryManagement.exception.AppException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        ErrorCode err = ErrorCode.UNAUTHENTICATED; // Default error code

        // Check if the exception is an instance of AppException
        if(authException instanceof JwtExpiredException) {
            //log.info("ABCBJBJB");
            err = ErrorCode.TOKEN_EXPIRED;
        }

        // Set the response status for expired token or other auth errors
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 Unauthorized
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Prepare the error response
        ResponseError resErr = ResponseError.builder()
                .message(err.getMessage())
                .error(err.getCode())
                .build();

        // Convert the response object to JSON string
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(resErr));

        // Force the response to be sent
        response.flushBuffer();
    }
}


