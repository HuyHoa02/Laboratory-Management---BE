package com.chris.LaboratoryManagement.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AgendaCreationRequest {
    String registrationId;
    Optional<String> laboratoryId = Optional.empty();
}
