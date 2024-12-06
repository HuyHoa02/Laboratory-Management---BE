package com.chris.LaboratoryManagement.repository;

import com.chris.LaboratoryManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String s);
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.id = 2")
    Optional<User> findAdmin();
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);
    Optional<User> findTopByOrderByIdDesc();
}
