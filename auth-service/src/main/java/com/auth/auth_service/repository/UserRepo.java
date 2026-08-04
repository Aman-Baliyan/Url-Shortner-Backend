package com.auth.auth_service.repository;

import com.auth.auth_service.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserData, Integer> {
    Optional<UserData> findByEmail(String email);
}
