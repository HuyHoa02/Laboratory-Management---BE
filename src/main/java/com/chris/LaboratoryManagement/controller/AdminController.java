package com.chris.LaboratoryManagement.controller;

import com.chris.LaboratoryManagement.dto.request.*;
import com.chris.LaboratoryManagement.dto.response.*;
import com.chris.LaboratoryManagement.entity.Semester;
import com.chris.LaboratoryManagement.entity.TimeSet;
import com.chris.LaboratoryManagement.repository.*;
import com.chris.LaboratoryManagement.service.*;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin")
public class AdminController {
    CourseService courseService;
    LaboratoryService laboratoryService;
    ClassService classService;
    RegistrationService registrationService;
    AgendaService agendaService;
    UserService userService;

    @PostMapping("/add-lecturer")
    public ApiResponse<UserResponse> createLecturer(@RequestBody @Valid UserCreationRequest request) throws MessagingException {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createLecturer(request))
                .message("The new lecturer had been added successfully")
                .build();
    }

    @PostMapping("/add-course")
    public ApiResponse<CourseResponse> addCourse(@RequestBody CourseCreationRequest request){
        return ApiResponse.<CourseResponse>builder()
                .result(courseService.addCourse(request))
                .message("The new course had been added successfully")
                .build();
    }

    @PostMapping("/add-laboratory")
    public ApiResponse<LaboratoryResponse> addLaboratory(@RequestBody LaboratoryCreationRequest request){
        return ApiResponse.<LaboratoryResponse>builder()
                .result(laboratoryService.addLaboratory(request))
                .message("The new laboratory had been added successfully")
                .build();
    }

    @PostMapping("/add-timeset")
    public ApiResponse addTimeSet(@RequestBody TimeSetCreationRequest request){
        return ApiResponse.builder()
                .message("The new timeset had been added successfully")
                .build();
    }
    @PostMapping("/add-class")
    public ApiResponse<ClassCreationResponse> addClass(@RequestBody ClassCreationRequest request){
        return ApiResponse.<ClassCreationResponse>builder()
                .result(classService.addClass(request))
                .build();
    }

    @PostMapping("/add-agenda")
    public ApiResponse<AgendaCreationResponse> addAgenda(@RequestBody AgendaCreationRequest request){
        return ApiResponse.<AgendaCreationResponse>builder()
                .result(agendaService.addAgenda(request))
                .build();
    }

    @PostMapping("/reject-registration")
    public ApiResponse<String> rejectRegistration(@RequestBody AgendaCreationRequest request){
        return ApiResponse.<String>builder()
                .result(registrationService.reject(request))
                .build();
    }




    @GetMapping("/get-registration")
    public ApiResponse<List<RegistrationCreationResponse>> getAllRegistration(){
        return ApiResponse.<List<RegistrationCreationResponse>>builder()
                .result(registrationService.getAll())
                .build();
    }

    @GetMapping("/get-info/{id}")
    public ApiResponse<UserResponse> getMyInfo(@PathVariable String id){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo(id))
                .build();
    }

    @PutMapping("/update-lecturer/{id}")
    public ApiResponse<UserResponse> updateInfo(@RequestBody UserUpdateRequest request
            ,@PathVariable String id){
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateInfo(request, id))
                .build();
    }

    @PutMapping("/update-course")
    public ApiResponse<CourseResponse> updateCourse(@RequestBody CourseUpdateRequest request){
        return ApiResponse.<CourseResponse>builder()
                .result(courseService.updateCourse(request))
                .build();
    }

    @PutMapping("/update-laboratory")
    public ApiResponse<LaboratoryResponse> updateCourse(@RequestBody LaboratoryUpdateRequest request){
        return ApiResponse.<LaboratoryResponse>builder()
                .result(laboratoryService.updateLaboratory(request))
                .build();
    }

    @DeleteMapping("/delete-lecturer/{id}")
    public ApiResponse<String> deleteLecturer(@PathVariable String id) {
        String result = userService.deleteLecturer(id); // Call service method to delete user
        return ApiResponse.<String>builder().result(result).build(); // Return empty response indicating success
    }

    @DeleteMapping("/delete-class/{id}")
    public ApiResponse<String> deleteClass(@PathVariable String id) {
        String result = classService.deleteClass(id); // Call service method to delete user
        return ApiResponse.<String>builder().result(result).build(); // Return empty response indicating success
    }
    @DeleteMapping("/delete-course/{id}")
    public ApiResponse<String> deleteCourse(@PathVariable String id) {
        String result = courseService.deleteCourse(id); // Call service method to delete course
        return ApiResponse.<String>builder().result(result).build(); // Return empty response indicating success
    }
    @DeleteMapping("/delete-laboratory/{id}")
    public ApiResponse<String> deleteLaboratory(@PathVariable String id) {
        String result = laboratoryService.deleteLaboratory(id); // Call service method to delete laboratory
        return ApiResponse.<String>builder().result(result).build(); // Return empty response indicating success
    }


}
