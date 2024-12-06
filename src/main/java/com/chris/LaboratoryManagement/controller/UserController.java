package com.chris.LaboratoryManagement.controller;

import com.chris.LaboratoryManagement.dto.request.SearchingRequest;
import com.chris.LaboratoryManagement.dto.request.UpdatePasswordRequest;
import com.chris.LaboratoryManagement.dto.request.UserCreationRequest;
import com.chris.LaboratoryManagement.dto.response.*;
import com.chris.LaboratoryManagement.mapper.UserMapper;
import com.chris.LaboratoryManagement.service.*;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/public")
public class UserController {
    UserService userService;
    CourseService courseService;
    ClassService classService;
    LaboratoryService laboratoryService;
    SessionService sessionService;
    SemesterService semesterService;
    TimeSetService timeSetService;
    AgendaService agendaService;


    @GetMapping("/get-course")
    public ApiResponse<List<CourseResponse>> getAllCourse(){
        return ApiResponse.<List<CourseResponse>>builder()
                .result(courseService.getAll())
                .build();
    }
    @GetMapping("/get-session")
    public ApiResponse<List<SessionCreationResponse>> getSession(){
        return ApiResponse.<List<SessionCreationResponse>>builder()
                .result(sessionService.getAll())
                .build();
    }

    @GetMapping("/get-semester")
    public ApiResponse<List<SemesterCreationResponse>> getSemester(){
        return ApiResponse.<List<SemesterCreationResponse>>builder()
                .result(semesterService.getSemester())
                .build();
    }

    @GetMapping("/get-class")
    public ApiResponse<List<ClassCreationResponse>> getAllClass(){
        return ApiResponse.<List<ClassCreationResponse>>builder()
                .result(classService.getAll())
                .build();
    }

    @GetMapping("/get-class/{id}")
    public ApiResponse<List<ClassCreationResponse>> getClassByLecturerId(@PathVariable String id){
        return ApiResponse.<List<ClassCreationResponse>>builder()
                .result(classService.getClassByLecturerId(id))
                .build();
    }

    @GetMapping("/get-timeset/{index}")
    public ApiResponse<TimeSetCreationResponse> getTimeSet(@PathVariable int index){
        return ApiResponse.<TimeSetCreationResponse>builder()
                .result(timeSetService.getTimeSetByIndex(index))
                .build();
    }

    @GetMapping("/get-all-timesets")
    public ApiResponse<List<TimeSetCreationResponse>> getTimeSets(){
        return ApiResponse.<List<TimeSetCreationResponse>>builder()
                .result(timeSetService.getTimeSets())
                .build();
    }

    @GetMapping("/get-laboratory")
    public ApiResponse<List<LaboratoryResponse>> getAllLabs(){
        return ApiResponse.<List<LaboratoryResponse>>builder()
                .result(laboratoryService.getAll())
                .build();
    }


    @GetMapping("/get-agenda/{index}")
    public ApiResponse<List<AgendaCreationResponse>> getAgenda(@PathVariable int index){
        return ApiResponse.<List<AgendaCreationResponse>>builder()
                .result(agendaService.getAgendaByCurrentTimeSet(index))
                .build();
    }

    @PostMapping("/get-agenda-by-input")
    public ApiResponse<List<AgendaCreationResponse>> getAgendaByInput(@RequestBody SearchingRequest request){
        return ApiResponse.<List<AgendaCreationResponse>>builder()
                .result(agendaService.getAgendaByInput(request))
                .build();
    }
    @GetMapping("/get-lecturers")
    public ApiResponse<List<UserResponse>> getLecturers(){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllLecturers())
                .build();
    }

}
