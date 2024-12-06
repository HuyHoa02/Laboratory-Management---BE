package com.chris.LaboratoryManagement.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassCreationResponse {
    String id;
    int size;
    String courseId;
    String lecturerId;
}
