package com.example.userauthenticationservice.Repos;

import com.example.userauthenticationservice.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface UserRepo extends JpaRepository<User,Long> {

   public Optional<User> findByEmail (String email);

}
