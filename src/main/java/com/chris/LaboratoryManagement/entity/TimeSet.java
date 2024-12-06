package com.chris.LaboratoryManagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class TimeSet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    LocalDate startDate;
    LocalDate endDate;
    int isCurrent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester")
    Semester semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolYear")
    SchoolYear schoolYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "week")
    Week week;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "timeSet", orphanRemoval = true)
    Set<Agenda> agenda = new HashSet<>();
}
