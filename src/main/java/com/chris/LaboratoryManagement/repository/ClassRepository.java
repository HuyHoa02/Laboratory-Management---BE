package com.chris.LaboratoryManagement.repository;

import com.chris.LaboratoryManagement.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ClassRepository extends JpaRepository<Class, String> {
    @Query("SELECT c.id FROM Class c WHERE c.id LIKE :courseId% ORDER BY c.id DESC LIMIT 1")
    String findLastClassIdByCourseId(@Param("courseId") String courseId);
    @Query("SELECT c FROM Class c WHERE c.user.id = :id")
    List<Class> findByLecturerId(@Param("id") String id);
}
