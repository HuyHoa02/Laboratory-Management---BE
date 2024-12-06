package com.chris.LaboratoryManagement.repository;

import com.chris.LaboratoryManagement.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    Optional<Session> findByName(String s);
}
