package com.chris.LaboratoryManagement.repository;

import com.chris.LaboratoryManagement.entity.Agenda;
import com.chris.LaboratoryManagement.entity.Laboratory;
import com.chris.LaboratoryManagement.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository

public interface AgendaRepository extends JpaRepository<Agenda, String> {
    boolean existsByLaboratoryAndSessionAndDate(Laboratory laboratory, Session session, LocalDate reservationDate);
    List<Agenda> findByTimeSetId(String timeSetId);

    @Query("SELECT a FROM Agenda a JOIN a.aClass c JOIN c.user u WHERE LOWER(c.course.name) LIKE LOWER(CONCAT('%', :courseName, '%'))")
    List<Agenda> findByCourseNameContaining(@Param("courseName") String courseName);

    @Query("SELECT a FROM Agenda a JOIN a.aClass c WHERE c.id LIKE CONCAT('%', :classId, '%')")
    List<Agenda> findByClassIdContaining(@Param("classId") String classId);

    @Query("SELECT a FROM Agenda a JOIN a.aClass c JOIN c.user u WHERE LOWER(u.firstname) LIKE LOWER(CONCAT('%', :lecturerName, '%')) OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :lecturerName, '%'))")
    List<Agenda> findByLecturerNameContaining(@Param("lecturerName") String lecturerName);


}
