package com.chris.LaboratoryManagement.service;

import com.chris.LaboratoryManagement.dto.request.LaboratoryCreationRequest;
import com.chris.LaboratoryManagement.dto.request.LaboratoryUpdateRequest;
import com.chris.LaboratoryManagement.dto.response.LaboratoryResponse;
import com.chris.LaboratoryManagement.entity.Course;
import com.chris.LaboratoryManagement.entity.Laboratory;
import com.chris.LaboratoryManagement.enums.ErrorCode;
import com.chris.LaboratoryManagement.exception.AppException;
import com.chris.LaboratoryManagement.mapper.LaboratoryMapper;
import com.chris.LaboratoryManagement.repository.LaboratoryRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class LaboratoryService {
    LaboratoryRepository laboratoryRepository;
    LaboratoryMapper laboratoryMapper;

    public LaboratoryResponse addLaboratory(LaboratoryCreationRequest request){
        laboratoryRepository.findById(request.getId()).ifPresent(laboratory -> {
            throw new AppException(ErrorCode.LABORATORY_EXISTED);
        });

        Laboratory laboratory = Laboratory.builder()
                .id(request.getId())
                .capacity(request.getCapacity())
                .build();
        laboratoryRepository.save(laboratory);
        return LaboratoryResponse.builder()
                .id(laboratory.getId())
                .capacity(laboratory.getCapacity())
                .build();
    }

    public List<LaboratoryResponse> getAll(){
        return laboratoryRepository.findAll().stream().map(laboratoryMapper::toLaboratoryResponse).toList();
    }

    public LaboratoryResponse updateLaboratory(LaboratoryUpdateRequest request){
        Optional<Laboratory> labChecker = laboratoryRepository.findById(request.getId());
        if(labChecker.isEmpty()){
            throw new AppException(ErrorCode.LABORATORY_NOT_EXISTED);
        }

        Laboratory laboratory = labChecker.get();
        laboratory.setCapacity(request.getCapacity());

        Laboratory savedLab = laboratoryRepository.save(laboratory);

        return laboratoryMapper.toLaboratoryResponse(savedLab);
    }

    public String deleteLaboratory(String id){
        Optional<Laboratory> laboratoryChecker = laboratoryRepository.findById(id);
        if(laboratoryChecker.isEmpty()){
            throw new AppException(ErrorCode.LABORATORY_NOT_EXISTED);
        }

        laboratoryRepository.deleteById(id);

        return "Laboratory has been deleted successfully";
    }
}
