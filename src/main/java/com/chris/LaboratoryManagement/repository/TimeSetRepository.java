package com.chris.LaboratoryManagement.repository;

import com.chris.LaboratoryManagement.entity.SchoolYear;
import com.chris.LaboratoryManagement.entity.Semester;
import com.chris.LaboratoryManagement.entity.TimeSet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository

public interface TimeSetRepository extends JpaRepository<TimeSet, String> {
    @Query("SELECT MAX(t.endDate) FROM TimeSet t")
    Optional<LocalDate> findLatestEndDate();
    @Modifying
    @Transactional
    @Query("UPDATE TimeSet t SET t.isCurrent = 0 WHERE t.isCurrent > 0")
    void resetIsCurrent();
    boolean existsBySchoolYearAndSemester(SchoolYear schoolYear, Semester semester);
    @Query("SELECT COUNT(t) > 0 FROM TimeSet t WHERE t.isCurrent > 0 AND :reservationDate BETWEEN t.startDate AND t.endDate")
    boolean existsByReservationDateWithinCurrentTimeSet(@Param("reservationDate") LocalDate reservationDate);
    @Query("SELECT t FROM TimeSet t WHERE t.isCurrent > 0 AND :reservationDate BETWEEN t.startDate AND t.endDate")
    Optional<TimeSet> findByReservationDate(@Param("reservationDate") LocalDate reservationDate);
    @Query("SELECT t.id FROM TimeSet t WHERE t.isCurrent = :isCurrent")
    String findIdsByIsCurrent(@Param("isCurrent") int isCurrent);
    Optional<TimeSet> findFirstByIsCurrent(int isCurrent);
    @Query("SELECT t FROM TimeSet t WHERE t.isCurrent != 0 ORDER BY t.isCurrent ASC")
    List<TimeSet> findAllOrderedByIsCurrent();

}
