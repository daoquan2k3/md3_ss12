package com.learn.project.md3_ss12.repository;

import com.learn.project.md3_ss12.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<Users, String> {
    Optional<Users> findByEmailOrPhone(String email, String phone);
    Optional<Users> findByEmail(String email);
}
