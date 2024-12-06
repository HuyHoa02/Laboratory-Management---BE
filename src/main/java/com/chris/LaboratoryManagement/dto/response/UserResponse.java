package com.chris.LaboratoryManagement.dto.response;

import com.chris.LaboratoryManagement.enums.GenderEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String firstname;
    String lastname;
    String username;
    GenderEnum gender;
    String email;
    String phoneNum;
    Set<RoleResponse> roles;
}
