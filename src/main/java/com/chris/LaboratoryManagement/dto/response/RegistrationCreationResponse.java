package com.chris.LaboratoryManagement.dto.response;

import com.chris.LaboratoryManagement.enums.StateEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationCreationResponse {
    String registrationId;
    String classId;
    String labId;
    String sessionName;
    LocalDate reservationDate;
    StateEnum state;
    Timestamp createdAt;
}
