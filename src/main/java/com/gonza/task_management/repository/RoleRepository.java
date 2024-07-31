package com.gonza.task_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gonza.task_management.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
