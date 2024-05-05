package com.example.userauthenticationservice.Services;

import com.example.userauthenticationservice.Exceptions.E_UserExists;
import com.example.userauthenticationservice.Models.User;
import org.springframework.util.MultiValueMap;
import org.antlr.v4.runtime.misc.Pair;

public interface iAuthService {
    public User signup(String email, String pwd) throws E_UserExists;
    public Pair<User, MultiValueMap<String,String>> login (String email, String pwd);
    public Boolean validate (Long userid,String token);
}
