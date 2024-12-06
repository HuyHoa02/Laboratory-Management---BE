package com.chris.LaboratoryManagement.repository;

import com.chris.LaboratoryManagement.entity.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface LaboratoryRepository extends JpaRepository<Laboratory, String> {
}
