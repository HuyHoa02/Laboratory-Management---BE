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
public class Laboratory {
    @Id
    String id;
    int capacity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "laboratory", orphanRemoval = true)
    Set<Registration> registrations = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "laboratory", orphanRemoval = true)
    Set<Agenda> agenda = new HashSet<>();
}
