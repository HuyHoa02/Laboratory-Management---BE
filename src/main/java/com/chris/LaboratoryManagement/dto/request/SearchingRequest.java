package com.chris.LaboratoryManagement.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchingRequest {
    String method;
    String input;
}
