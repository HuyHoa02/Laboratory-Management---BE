package com.chris.LaboratoryManagement.repository;

import com.chris.LaboratoryManagement.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface RegistrationRepository extends JpaRepository<Registration, String> {
    @Query("SELECT r FROM Registration r ORDER BY r.state ASC")
    List<Registration> findAllAscOfState();
}
