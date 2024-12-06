package com.chris.LaboratoryManagement.service;

import com.chris.LaboratoryManagement.dto.request.ClassCreationRequest;
import com.chris.LaboratoryManagement.dto.response.ClassCreationResponse;
import com.chris.LaboratoryManagement.entity.Class;
import com.chris.LaboratoryManagement.enums.ErrorCode;
import com.chris.LaboratoryManagement.exception.AppException;
import com.chris.LaboratoryManagement.mapper.ClassMapper;
import com.chris.LaboratoryManagement.repository.*;
import com.chris.LaboratoryManagement.utils.Helper;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ClassService {
    ClassRepository classRepository;
    CourseRepository courseRepository;
    UserRepository userRepository;
    ClassMapper classMapper;
    Helper helper;

    private final RoleRepository roleRepository;

    public ClassCreationResponse addClass(ClassCreationRequest request){
        var courseIdChecker = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_EXISTED));

        var lecturerIdChecker = userRepository.findById(request.getLecturerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        var newClassId = helper.generateNextClassId(request.getCourseId());

        Class aClass = Class.builder()
                .id(newClassId)
                .size(request.getSize())
                .course(courseIdChecker)
                .user(lecturerIdChecker)
                .build();



        classRepository.save(aClass);
        return ClassCreationResponse.builder()
                .id(aClass.getId())
                .size(aClass.getSize())
                .courseId(aClass.getCourse().getId())
                .lecturerId(aClass.getUser().getId())
                .build();
    }

    public List<ClassCreationResponse> getAll(){
        return classRepository.findAll().stream().map(aClass -> {
            var classResponse = classMapper.toClassResponse(aClass);
            classResponse.setLecturerId(aClass.getUser().getId());
            classResponse.setCourseId(aClass.getCourse().getId());
            return classResponse;
        }).toList();
    }

    public List<ClassCreationResponse> getClassByLecturerId(String id) {
        return classRepository.findByLecturerId(id).stream().map(aClass -> {
            var classResponse = classMapper.toClassResponse(aClass);
            classResponse.setLecturerId(aClass.getUser().getId());
            classResponse.setCourseId(aClass.getCourse().getId());
            return classResponse;
        }).toList();
    }

    public String deleteClass(String id){
        Optional<Class> classChecker = classRepository.findById(id);
        if(classChecker.isEmpty()){
            throw new AppException(ErrorCode.CLASS_NOT_EXISTED);
        }
        classRepository.deleteById(id);

        return "Class has been remove successfully!";
    }
}
