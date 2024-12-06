package com.chris.LaboratoryManagement.entity;

import com.chris.LaboratoryManagement.enums.StateEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    StateEnum state;
    Timestamp createdAt;
    Timestamp hanldeDate;
    LocalDate reservationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session")
    Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class")
    Class aClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratory")
    Laboratory laboratory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin")
    User admin;


}
