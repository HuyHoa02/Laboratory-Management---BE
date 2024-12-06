package com.chris.LaboratoryManagement.service;

import com.chris.LaboratoryManagement.dto.request.TimeSetCreationRequest;
import com.chris.LaboratoryManagement.dto.response.TimeSetCreationResponse;
import com.chris.LaboratoryManagement.entity.SchoolYear;
import com.chris.LaboratoryManagement.entity.Semester;
import com.chris.LaboratoryManagement.entity.TimeSet;
import com.chris.LaboratoryManagement.entity.Week;
import com.chris.LaboratoryManagement.enums.ErrorCode;
import com.chris.LaboratoryManagement.exception.AppException;
import com.chris.LaboratoryManagement.mapper.TimeSetMapper;
import com.chris.LaboratoryManagement.repository.SchoolYearRepository;
import com.chris.LaboratoryManagement.repository.SemesterRepository;
import com.chris.LaboratoryManagement.repository.TimeSetRepository;
import com.chris.LaboratoryManagement.repository.WeekRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class TimeSetService {
    TimeSetRepository timeSetRepository;
    SchoolYearRepository schoolYearRepository;
    SemesterRepository semesterRepository;
    WeekRepository weekRepository;
    TimeSetMapper timeSetMapper;


    public void createTimeSet(TimeSetCreationRequest request) {
        SchoolYear schoolYear = getOrCreateSchoolYear(request.getSchoolYearName());
        Semester semester = semesterRepository.findByName(request.getSemesterName())
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_EXISTED));

        if (timeSetRepository.existsBySchoolYearAndSemester(schoolYear, semester)) {
            throw new AppException(ErrorCode.TIMESLOT_ALREADY_EXISTS);
        }

        List<Week> weeks = weekRepository.findAll();

        List<TimeSet> timeSets = createTimeSetsForSchoolYear(schoolYear, semester, weeks, request.getStartDate());

    }

    private SchoolYear getOrCreateSchoolYear(String schoolYearName) {
        return schoolYearRepository.findByName(schoolYearName)
                .orElseGet(() -> schoolYearRepository.save(SchoolYear.builder()
                        .name(schoolYearName)
                        .build()));
    }

    private List<TimeSet> createTimeSetsForSchoolYear(SchoolYear schoolYear, Semester semester, List<Week> weeks, LocalDate startDate) {
        Optional<LocalDate> latestEndDate = timeSetRepository.findLatestEndDate();

        // Check if the provided startDate is after the latest endDate
        if (latestEndDate.isPresent() && !startDate.isAfter(latestEndDate.get())) {
            throw new AppException(ErrorCode.INVALID_STARTDATE);
        }

        List<TimeSet> timeSets = new ArrayList<>();
        LocalDate currentStartDate = startDate;

        // Reset all existing TimeSets' isCurrent field to 0 before adding new ones
        timeSetRepository.resetIsCurrent();

        for (int i = 0; i < weeks.size(); i++) {
            Week week = weeks.get(i);

            LocalDate endDate = currentStartDate.plusDays(6);

            int isCurrentValue = i + 1; // Set sequential value for isCurrent, adjust if needed

            TimeSet timeSet = TimeSet.builder()
                    .schoolYear(schoolYear)
                    .semester(semester)
                    .week(week)
                    .isCurrent(isCurrentValue) // Set isCurrent based on the week order (or dynamically)
                    .startDate(currentStartDate)
                    .endDate(endDate)
                    .build();

            timeSets.add(timeSet);
            timeSetRepository.save(timeSet);

            currentStartDate = currentStartDate.plusWeeks(1);
        }

        return timeSets;
    }

        public TimeSetCreationResponse getTimeSetByIndex(int index) {
            return timeSetRepository.findFirstByIsCurrent(index)
                    .map(timeSetMapper::toTimeSetResponse) // Map to DTO
                    .orElseThrow(() -> new AppException(ErrorCode.TIMESET_NOT_EXISTED));
        }

        public List<TimeSetCreationResponse> getTimeSets(){
            return timeSetRepository.findAllOrderedByIsCurrent()
                    .stream()
                    .map(timeSet -> TimeSetCreationResponse.builder()
                            .startDate(timeSet.getStartDate())
                            .isCurrent(timeSet.getIsCurrent())
                            .build())
                    .toList();
        }
}
