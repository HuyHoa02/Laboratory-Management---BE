package com.chris.LaboratoryManagement.service;

import com.chris.LaboratoryManagement.dto.request.RoleRequest;
import com.chris.LaboratoryManagement.dto.response.RoleResponse;
import com.chris.LaboratoryManagement.mapper.RoleMapper;
import com.chris.LaboratoryManagement.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request){
        var role = roleMapper.toRole(request);

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getAll(){
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void delete(String id){
        roleRepository.deleteById(id);
    }
}
