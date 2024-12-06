package com.chris.LaboratoryManagement.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassCreationRequest {
    int size;
    String courseId;
    String lecturerId;
}
