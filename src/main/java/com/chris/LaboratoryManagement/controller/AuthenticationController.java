package com.chris.LaboratoryManagement.controller;

import com.chris.LaboratoryManagement.dto.request.*;
import com.chris.LaboratoryManagement.dto.response.ApiResponse;
import com.chris.LaboratoryManagement.dto.response.AuthenticationResponse;
import com.chris.LaboratoryManagement.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/signin")
    ApiResponse<AuthenticationResponse> signin(@RequestBody AuthenticationRequest request){
        var result = authenticationService.login(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }


    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }


    @PostMapping("/refreshToken")
    ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/verify-email")
    ApiResponse<AuthenticationResponse> verifyEmail(@RequestBody EmailVerifyRequest request){
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.verifyEmail(request))
                .build();
    }
}

