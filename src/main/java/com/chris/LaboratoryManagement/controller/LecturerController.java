package com.chris.LaboratoryManagement.controller;

import com.chris.LaboratoryManagement.dto.request.RegistrationCreationRequest;
import com.chris.LaboratoryManagement.dto.request.UpdatePasswordRequest;
import com.chris.LaboratoryManagement.dto.response.*;
import com.chris.LaboratoryManagement.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/lecturer")
public class LecturerController {
    UserService userService;
    RegistrationService registrationService;

    @PostMapping("/update-password")
    public ApiResponse<String> updatePassword(@RequestBody UpdatePasswordRequest request){
        return ApiResponse.<String>builder()
                .result(userService.updatePassword(request))
                .build();
    }
    @PostMapping("/add-registrations")
    public ApiResponse<List<RegistrationCreationResponse>> addRegistrations(@RequestParam("file") MultipartFile file) {
        // Check if the file is empty or not a valid Excel file
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".xlsx")) {
            return ApiResponse.<List<RegistrationCreationResponse>>builder()
                    .message("Invalid file. Please upload a valid Excel file.")
                    .build();
        }

        // Call the service to read the Excel and save registrations
        List<RegistrationCreationResponse> responses = registrationService.readExcel(file);

        // Return the successful response with the list of RegistrationCreationResponse objects
        return ApiResponse.<List<RegistrationCreationResponse>>builder()
                .result(responses)
                .message("Registrations added successfully.")
                .build();
    }
    @PostMapping("/add-registration")
    public ApiResponse<RegistrationCreationResponse> addRegistration(@RequestBody RegistrationCreationRequest request){
        return ApiResponse.<RegistrationCreationResponse>builder()
                .result(registrationService.addRegistration(request))
                .build();
    }

}
