package com.chris.LaboratoryManagement.repository;

import com.chris.LaboratoryManagement.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
}
