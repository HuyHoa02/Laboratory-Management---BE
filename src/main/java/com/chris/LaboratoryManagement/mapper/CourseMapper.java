package com.chris.LaboratoryManagement.mapper;

import com.chris.LaboratoryManagement.dto.request.CourseCreationRequest;
import com.chris.LaboratoryManagement.dto.response.CourseResponse;
import com.chris.LaboratoryManagement.entity.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toCourse(CourseCreationRequest request);
    CourseResponse toCourseResponse(Course course);
}
