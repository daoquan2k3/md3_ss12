package com.learn.project.md3_ss12.repository;

import com.learn.project.md3_ss12.entity.Role;
import com.learn.project.md3_ss12.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}
