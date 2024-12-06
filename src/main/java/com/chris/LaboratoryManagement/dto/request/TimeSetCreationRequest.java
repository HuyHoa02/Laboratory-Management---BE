package com.chris.LaboratoryManagement.dto.request;

import com.chris.LaboratoryManagement.entity.SchoolYear;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeSetCreationRequest {
    LocalDate startDate;
    String schoolYearName;
    String semesterName;
}
