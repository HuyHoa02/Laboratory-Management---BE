package com.chris.LaboratoryManagement.service;

import com.chris.LaboratoryManagement.dto.request.SemesterCreationResquest;
import com.chris.LaboratoryManagement.dto.response.SemesterCreationResponse;
import com.chris.LaboratoryManagement.entity.Semester;
import com.chris.LaboratoryManagement.enums.ErrorCode;
import com.chris.LaboratoryManagement.exception.AppException;
import com.chris.LaboratoryManagement.repository.SemesterRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class SemesterService {
    SemesterRepository semesterRepository;

    public SemesterCreationResponse addSemester(SemesterCreationResquest request){
        semesterRepository.findByName(request.getName()).ifPresent(semester -> {
            throw new AppException(ErrorCode.SEMESTER_EXISTED);
        });

        Semester semester = Semester.builder()
                .name(request.getName())
                .build();
        semesterRepository.save(semester);
        return SemesterCreationResponse.builder()
                .name(semester.getName())
                .build();
    }

    public List<SemesterCreationResponse> getSemester(){
        return semesterRepository.findAll().stream().map(semester -> {
            return SemesterCreationResponse.builder()
                    .name(semester.getName())
                    .build();
        }).toList();
    }
}
