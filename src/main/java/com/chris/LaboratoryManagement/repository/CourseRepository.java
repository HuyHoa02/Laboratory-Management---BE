package com.chris.LaboratoryManagement.repository;

import com.chris.LaboratoryManagement.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CourseRepository extends JpaRepository<Course, String> {
}
