package com.chris.LaboratoryManagement.repository;

import com.chris.LaboratoryManagement.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SemesterRepository extends JpaRepository<Semester, String> {
    Optional<Semester> findByName(String s);
}
