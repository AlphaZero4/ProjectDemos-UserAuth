package com.example.userauthenticationservice.Services;

import com.example.userauthenticationservice.Exceptions.E_UserExists;
import com.example.userauthenticationservice.Models.User;

public interface iAuthService {
    public User signup(String email, String pwd) throws E_UserExists;
    public User login (String email, String pwd);
}
