package com.chris.LaboratoryManagement.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationCreationRequest {
    String classId;
    String labId;
    String sessionName;
    LocalDate reservationDate;
}
