package com.example.userauthenticationservice.Controller;

import com.example.userauthenticationservice.Dtos.UserReqDto;
import com.example.userauthenticationservice.Dtos.UserRespDto;
import com.example.userauthenticationservice.Models.User;
import com.example.userauthenticationservice.Services.iAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    //sign up
    //login
    //forgot pwd
    //logout
    @Autowired
    iAuthService authService;
    @PostMapping("/auth/signup")
    public ResponseEntity<UserRespDto> signup(@RequestBody  UserReqDto dto){
        //UserRespDto response;
      try{ User user= authService.signup(dto.getEmail(), dto.getPwd());
        UserRespDto responseDto = UserToDto(user);
        //return null;
        return new ResponseEntity<UserRespDto>(responseDto, HttpStatus.OK);}
        catch(Exception E){
            System.out.println("User already Exists");
            return null;
        }
    }
    @PostMapping("/auth/login")
    public ResponseEntity<UserRespDto> login(UserReqDto dto){

        return null;
    }
public UserRespDto UserToDto(User user){
        UserRespDto response = new UserRespDto();
        response.setEmail(user.getEmail());
        response.setRoles(user.getRoles());
return response;

}

}
