package com.chris.LaboratoryManagement.dto.request;

import com.chris.LaboratoryManagement.enums.GenderEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String username;
    String password;
    String firstname;
    String lastname;
    GenderEnum gender;
    String email;
    String phoneNum;
}
