package com.chris.LaboratoryManagement.mapper;

import com.chris.LaboratoryManagement.dto.request.RoleRequest;
import com.chris.LaboratoryManagement.dto.response.RoleResponse;
import com.chris.LaboratoryManagement.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);

}
