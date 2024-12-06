package com.chris.LaboratoryManagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token {
    @Id
    String id;
    String refreshToken;
    boolean role;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;
}
