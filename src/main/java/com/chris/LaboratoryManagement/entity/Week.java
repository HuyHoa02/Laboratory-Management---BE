package com.chris.LaboratoryManagement.entity;

import jakarta.persistence.*;
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
public class Week {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "week", orphanRemoval = true)
    Set<TimeSet> timeSets = new HashSet<>();
}
