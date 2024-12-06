package com.chris.LaboratoryManagement.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Course {
    @Id
    String id;
    String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course", orphanRemoval = true)
    Set<Class> classes = new HashSet<>();
}
