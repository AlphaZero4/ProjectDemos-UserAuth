package com.example.userauthenticationservice.Services;

import com.example.userauthenticationservice.Repos.UserRepo;
import com.example.userauthenticationservice.security.Models.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.userauthenticationservice.Models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //find user from repo
        // ret if !exist
        //vailidate if exists
        Optional<User> optionalUser = userRepo.findByEmail(email);

        if(optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("Not Found");
        }

        User user = optionalUser.get();
        return new CustomUserDetails(user);
    }
}
