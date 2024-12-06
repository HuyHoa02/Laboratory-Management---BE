package com.chris.LaboratoryManagement.repository;

import com.chris.LaboratoryManagement.entity.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface WeekRepository extends JpaRepository<Week, String> {
    Optional<Week> findByName(String s);
}
