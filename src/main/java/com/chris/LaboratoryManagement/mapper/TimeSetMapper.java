package com.chris.LaboratoryManagement.mapper;

import com.chris.LaboratoryManagement.dto.request.TimeSetCreationRequest;
import com.chris.LaboratoryManagement.dto.request.UserCreationRequest;
import com.chris.LaboratoryManagement.dto.response.TimeSetCreationResponse;
import com.chris.LaboratoryManagement.dto.response.UserResponse;
import com.chris.LaboratoryManagement.entity.TimeSet;
import com.chris.LaboratoryManagement.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TimeSetMapper {
    TimeSet toTimeSet(TimeSetCreationRequest request);
    TimeSetCreationResponse toTimeSetResponse(TimeSet timeSet);
}
