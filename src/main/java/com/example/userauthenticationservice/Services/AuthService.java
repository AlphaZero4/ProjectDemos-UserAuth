package com.example.userauthenticationservice.Services;


import com.example.userauthenticationservice.Exceptions.E_UserExists;
import com.example.userauthenticationservice.Models.Session;
import com.example.userauthenticationservice.Models.User;
import com.example.userauthenticationservice.Repos.SessionRepo;
import com.example.userauthenticationservice.Repos.UserRepo;
import org.antlr.v4.runtime.misc.Pair;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;



@Service
public class AuthService implements iAuthService{

@Autowired
    private UserRepo repo;
@Autowired
private BCryptPasswordEncoder encoder;

@Autowired
private SessionRepo sessionRepo;





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
    public Pair<User, MultiValueMap<String,String>> login(String email, String pwd){
        Optional<User> user = repo.findByEmail(email);
        if(user.isEmpty()){return null;}
        User check_user = user.get();
        Pair<Integer,String> p;
//  if(!check_user.getPassword().equals(pwd)){
if(!encoder.matches(pwd,check_user.getPassword())){

    return null;
}
//token logic - return pair<user,multivaluemap> - map headers<string,string>
        // signatture gen - Macaalgorithm - hs256
        // SecretKey - algo.key.build()
        // jwts.builder().content().signWith(secret).compact()

        MacAlgorithm algorithm = Jwts.SIG.HS256;
        SecretKey secret = algorithm.key().build();
        Map<String,Object> jwt = new HashMap<>();
        jwt.put("email",check_user.getEmail());
        jwt.put("roles",check_user.getRoles());
        long nowinms = System.currentTimeMillis();
        jwt.put("createdAt",nowinms);
        jwt.put("expiry",nowinms+10000);
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();

        String token = Jwts.builder().claims(jwt).signWith(secret).compact();
        headers.add(HttpHeaders.SET_COOKIE,token);

        Session session= new Session();
        session.setUser(check_user);
        session.setToken(token);
        session.setExpiry(new Date(nowinms+10000));
        session.setCreatedAt(new Date(nowinms));
        Session saved_session = sessionRepo.save(session);

        return new Pair<User,MultiValueMap<String,String>>(check_user,headers);

    }
}
