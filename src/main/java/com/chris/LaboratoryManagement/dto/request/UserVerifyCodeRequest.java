package com.chris.LaboratoryManagement.dto.request;

public record UserVerifyCodeRequest(
        String code,
        String userId
        )
{ }
