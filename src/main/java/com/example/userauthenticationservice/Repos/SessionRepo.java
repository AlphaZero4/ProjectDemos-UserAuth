package com.example.userauthenticationservice.Repos;

import com.example.userauthenticationservice.Models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepo extends JpaRepository<Session,Long> {
    Optional<Session> findByTokenAndUser_Id(String token, Long id);
}
