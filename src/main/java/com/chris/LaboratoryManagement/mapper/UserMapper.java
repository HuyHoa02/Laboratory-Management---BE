package com.chris.LaboratoryManagement.mapper;

import com.chris.LaboratoryManagement.dto.request.UserCreationRequest;
import com.chris.LaboratoryManagement.dto.response.UserResponse;
import com.chris.LaboratoryManagement.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
}
