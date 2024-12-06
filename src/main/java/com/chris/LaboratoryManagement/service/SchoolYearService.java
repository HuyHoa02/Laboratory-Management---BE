package com.chris.LaboratoryManagement.service;

import com.chris.LaboratoryManagement.dto.request.SchoolYearCreationRequest;
import com.chris.LaboratoryManagement.dto.response.SchoolYearCreationResponse;
import com.chris.LaboratoryManagement.entity.SchoolYear;
import com.chris.LaboratoryManagement.enums.ErrorCode;
import com.chris.LaboratoryManagement.exception.AppException;
import com.chris.LaboratoryManagement.repository.SchoolYearRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class SchoolYearService {
    SchoolYearRepository schoolYearRepository;

    public SchoolYearCreationResponse addSchoolYear(SchoolYearCreationRequest request){
        schoolYearRepository.findByName(request.getName()).ifPresent(schoolYear -> {
            throw new AppException(ErrorCode.SCHOOLYEAR_EXISTED);
        });

        SchoolYear schoolYear = SchoolYear.builder()
                .name(request.getName())
                .build();
        schoolYearRepository.save(schoolYear);
        return SchoolYearCreationResponse.builder()
                .id(schoolYear.getId())
                .name(schoolYear.getName())
                .build();
    }
}
