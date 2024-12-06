package com.chris.LaboratoryManagement.service;

import com.chris.LaboratoryManagement.dto.request.CourseCreationRequest;
import com.chris.LaboratoryManagement.dto.request.CourseUpdateRequest;
import com.chris.LaboratoryManagement.dto.response.CourseResponse;
import com.chris.LaboratoryManagement.entity.Course;
import com.chris.LaboratoryManagement.enums.ErrorCode;
import com.chris.LaboratoryManagement.exception.AppException;
import com.chris.LaboratoryManagement.mapper.CourseMapper;
import com.chris.LaboratoryManagement.repository.CourseRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CourseService {
    CourseRepository courseRepository;
    CourseMapper courseMapper;

    public CourseResponse addCourse(CourseCreationRequest request){
        courseRepository.findById(request.getId()).ifPresent(course -> {
            throw new AppException(ErrorCode.COURSE_EXISTED);
        });

        Course course = Course.builder()
                .id(request.getId())
                .name(request.getName())
                .build();
        courseRepository.save(course);
        return CourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .build();
    }

    public List<CourseResponse> getAll(){
        return courseRepository.findAll().stream().map(courseMapper::toCourseResponse).toList();
    }

    public CourseResponse updateCourse(CourseUpdateRequest request){
        Optional<Course> courseChecker = courseRepository.findById(request.getId());
        if(courseChecker.isEmpty()){
            throw new AppException(ErrorCode.COURSE_NOT_EXISTED);
        }

        Course course = courseChecker.get();
        course.setName(request.getName());

        Course savedCourse = courseRepository.save(course);

        return courseMapper.toCourseResponse(savedCourse);
    }

    public String deleteCourse(String id){
        Optional<Course> courseChecker = courseRepository.findById(id);
        if(courseChecker.isEmpty()){
            throw new AppException(ErrorCode.COURSE_NOT_EXISTED);
        }

        courseRepository.deleteById(id);

        return "Course has been deleted successfully";
    }
}
