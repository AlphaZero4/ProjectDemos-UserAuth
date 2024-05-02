package com.example.userauthenticationservice.Services;

import com.example.userauthenticationservice.Exceptions.E_UserExists;
import com.example.userauthenticationservice.Models.User;
import com.example.userauthenticationservice.Repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements iAuthService{

@Autowired
    private UserRepo repo;
@Autowired
private BCryptPasswordEncoder encoder;





    @Override
    public User signup(String email, String pwd) throws E_UserExists {
        Optional<User> user = repo.findByEmail(email);
        if(user.isEmpty()){
            User newuser = new User();
            //newuser.setEmail(email);
            newuser.setPassword(encoder.encode(pwd));
            newuser.setPassword(pwd);
            User saveduser = repo.save(newuser);
            return saveduser;
        }
        else{
            //throw new E_UserExists("User already exists!");
            return user.get();
        }
        //return null;
    }
    public User login(String email,String pwd){
        Optional<User> user = repo.findByEmail(email);
        if(user.isEmpty()){return null;}
        User check_user = user.get();
//  if(!check_user.getPassword().equals(pwd)){
if(!encoder.matches(pwd,check_user.getPassword())){

    return null;
}
        return check_user;

    }
}
