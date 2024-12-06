package com.chris.LaboratoryManagement.entity;

import com.chris.LaboratoryManagement.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {

    @Id
    @GeneratedValue
    Long id;

    @Enumerated(EnumType.STRING)
    RoleEnum name;

    @ManyToMany(mappedBy = "roles")
    Set<User> users = new HashSet<>();
}