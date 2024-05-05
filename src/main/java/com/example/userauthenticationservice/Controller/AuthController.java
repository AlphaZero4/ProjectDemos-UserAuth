package com.example.userauthenticationservice.Controller;

import com.example.userauthenticationservice.Dtos.UserReqDto;
import com.example.userauthenticationservice.Dtos.UserRespDto;
import com.example.userauthenticationservice.Dtos.ValidateReqDto;
import com.example.userauthenticationservice.Models.User;
import com.example.userauthenticationservice.Services.iAuthService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/auth")
@RestController
public class AuthController { 
    //sign up
    //login
    //forgot pwd
    //logout
    @Autowired
    iAuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<UserRespDto> signup(@RequestBody  UserReqDto dto){
        //UserRespDto response;
        System.out.println("test1");
      try{ User user= authService.signup(dto.getEmail(), dto.getPwd());
        UserRespDto responseDto = UserToDto(user);
        //return null;
        return new ResponseEntity<UserRespDto>(responseDto, HttpStatus.OK);}
        catch(Exception E){
            System.out.println("User already Exists");
            return null;
        }
    }
    @PostMapping("/login")
    public ResponseEntity<UserRespDto> login(@RequestBody UserReqDto dto){
try {
    Pair<User, MultiValueMap<String,String>> UserHeader = authService.login(dto.getEmail(), dto.getPwd());
    UserRespDto response = UserToDto(UserHeader.a);
    return new ResponseEntity<>(response,UserHeader.b,HttpStatus.OK);
}
catch (Exception e){
    System.out.println("exception");
    return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);}

    }

public UserRespDto UserToDto(User user){
        UserRespDto response = new UserRespDto();
        response.setEmail(user.getEmail());
        response.setRoles(user.getRoles());
return response;

}

@PostMapping("/validate")
public ResponseEntity<Boolean> validate(@RequestBody ValidateReqDto request){

    Boolean response = authService.validate(request.getUserid(),request.getToken());
    return new ResponseEntity<>(response,HttpStatus.OK);


}
    @GetMapping("/test")
    public ResponseEntity<String> login(){
        String s= "success";
        return new ResponseEntity<String>(s,HttpStatus.OK);

    }

}
