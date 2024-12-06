package com.chris.LaboratoryManagement.mapper;

import com.chris.LaboratoryManagement.dto.request.RegistrationCreationRequest;
import com.chris.LaboratoryManagement.dto.request.RoleRequest;
import com.chris.LaboratoryManagement.dto.response.RegistrationCreationResponse;
import com.chris.LaboratoryManagement.dto.response.RoleResponse;
import com.chris.LaboratoryManagement.entity.Registration;
import com.chris.LaboratoryManagement.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegistrationMapper {
    Registration toRegistration(RegistrationCreationRequest request);
    RegistrationCreationResponse toRegistrationResponse(Registration registration);

}
