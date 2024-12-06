package com.chris.LaboratoryManagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    LocalDate date;
    boolean available;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id")
    Registration registration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session")
    Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeSet")
    TimeSet timeSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class")
    Class aClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratory")
    Laboratory laboratory;
}
