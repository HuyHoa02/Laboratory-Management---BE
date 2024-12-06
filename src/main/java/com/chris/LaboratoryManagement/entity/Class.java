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
    public class Class {
        @Id
        String id;
        int size;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "lecturer")
        User user;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "course")
        Course course;

        @OneToMany(cascade = CascadeType.ALL, mappedBy = "aClass", orphanRemoval = true)
        Set<Registration> registrations = new HashSet<>();

        @OneToMany(cascade = CascadeType.ALL, mappedBy = "aClass", orphanRemoval = true)
        Set<Agenda> agenda = new HashSet<>();
    }
