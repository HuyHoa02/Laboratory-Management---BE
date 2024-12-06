package com.chris.LaboratoryManagement.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AgendaCreationResponse {
    LocalDate date;
    String labId;
    String sessionName;
    String classId;
    String courseName;
    String lecturerName;
    boolean available;
}
