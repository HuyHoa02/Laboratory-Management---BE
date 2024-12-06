package com.chris.LaboratoryManagement.service;

import com.chris.LaboratoryManagement.dto.request.WeekCreationRequest;
import com.chris.LaboratoryManagement.dto.response.WeekCreationResponse;
import com.chris.LaboratoryManagement.entity.Week;
import com.chris.LaboratoryManagement.enums.ErrorCode;
import com.chris.LaboratoryManagement.exception.AppException;
import com.chris.LaboratoryManagement.repository.WeekRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class WeekService {
    WeekRepository weekRepository;

    public WeekCreationResponse addWeek(WeekCreationRequest request){
        weekRepository.findByName(request.getName()).ifPresent(week -> {
            throw new AppException(ErrorCode.WEEK_EXISTED);
        });

        Week week = Week.builder()
                .name(request.getName())
                .build();
        weekRepository.save(week);
        return WeekCreationResponse.builder()
                .id(week.getId())
                .name(week.getName())
                .build();
    }
}
