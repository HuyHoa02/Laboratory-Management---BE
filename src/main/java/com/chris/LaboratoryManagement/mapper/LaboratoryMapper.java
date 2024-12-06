package com.chris.LaboratoryManagement.mapper;

import com.chris.LaboratoryManagement.dto.request.LaboratoryCreationRequest;
import com.chris.LaboratoryManagement.dto.response.LaboratoryResponse;
import com.chris.LaboratoryManagement.entity.Laboratory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LaboratoryMapper {
    Laboratory toLaboratory(LaboratoryCreationRequest request);
    LaboratoryResponse toLaboratoryResponse(Laboratory laboratory);
}
