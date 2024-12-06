package com.chris.LaboratoryManagement.repository;

import com.chris.LaboratoryManagement.entity.Role;
import com.chris.LaboratoryManagement.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(RoleEnum s);
}
