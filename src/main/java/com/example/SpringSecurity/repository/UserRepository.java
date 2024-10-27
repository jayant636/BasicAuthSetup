package com.example.SpringSecurity.repository;

import com.example.SpringSecurity.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserRepository,Long> {
    Optional<UserEntity> findByEmail(String email);
}
