package com.chris.LaboratoryManagement.mapper;

import com.chris.LaboratoryManagement.dto.request.ClassCreationRequest;
import com.chris.LaboratoryManagement.dto.response.ClassCreationResponse;
import org.mapstruct.Mapper;
import com.chris.LaboratoryManagement.entity.Class;

@Mapper(componentModel = "spring")
public interface ClassMapper {
    Class toClass(ClassCreationRequest request);
    ClassCreationResponse toClassResponse(Class aclass);
}
