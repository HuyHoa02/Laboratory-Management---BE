package com.chris.LaboratoryManagement.entity;

import com.chris.LaboratoryManagement.enums.GenderEnum;
import com.chris.LaboratoryManagement.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    String id;
    String username;
    String password;
    String firstname;
    String lastname;
    GenderEnum gender;
    String email;
    String phoneNum;
    LocalDate createdAt;


    Boolean verified = false;

    String verificationCode;
    LocalDateTime verificationExpiry;


    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
//    @JsonIgnore
    Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    Set<Class> classes = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "admin", orphanRemoval = true)
    Set<Registration> registrations = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lecturer", orphanRemoval = true)
    Set<Notification> notifications = new HashSet<>();

    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }
}
