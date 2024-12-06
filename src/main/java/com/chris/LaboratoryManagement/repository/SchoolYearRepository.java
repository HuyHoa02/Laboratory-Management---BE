package com.chris.LaboratoryManagement.repository;

import com.chris.LaboratoryManagement.entity.SchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface SchoolYearRepository extends JpaRepository<SchoolYear, String> {
    Optional<SchoolYear> findByName(String s);
}
