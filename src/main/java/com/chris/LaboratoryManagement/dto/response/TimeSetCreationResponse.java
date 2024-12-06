package com.chris.LaboratoryManagement.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeSetCreationResponse {
    LocalDate startDate;
    int isCurrent;
}
