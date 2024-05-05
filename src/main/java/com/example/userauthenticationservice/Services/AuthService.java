package com.example.userauthenticationservice.Services;


import com.example.userauthenticationservice.Exceptions.E_UserExists;
import com.example.userauthenticationservice.Models.Roles;
import com.example.userauthenticationservice.Models.Session;
import com.example.userauthenticationservice.Models.User;
import com.example.userauthenticationservice.Repos.SessionRepo;
import com.example.userauthenticationservice.Repos.UserRepo;
import com.example.userauthenticationservice.enums.SessionStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
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
import java.util.*;


@Service
public class AuthService implements iAuthService{

@Autowired
    private UserRepo repo;
@Autowired

    private BCryptPasswordEncoder encoder;



@Autowired
private SessionRepo sessionRepo;
@Autowired
private SecretKey secret;





    @Override
    public User signup(String email, String pwd) throws E_UserExists
     {
        Optional<User> user = repo.findByEmail(email);
        if(user.isEmpty()){

            User newuser = new User();
            newuser.setEmail(email);
            newuser.setPassword(encoder.encode(pwd));
            newuser.setPassword(pwd);
            Roles userRole = new Roles();

            userRole.setValue("USER");
            newuser.setRoles((Set<Roles>) userRole);
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


        Map<String,Object> jwt = new HashMap<>();
        jwt.put("email",check_user.getEmail());
        jwt.put("roles",check_user.getRoles());
        long nowinms = System.currentTimeMillis();
        jwt.put("createdAt",nowinms);
        jwt.put("expiry",nowinms+10000000);
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();


  //      MacAlgorithm algorithm = Jwts.SIG.HS256;
    //    SecretKey secret = algorithm.key().build();
        String token = Jwts.builder().claims(jwt).signWith(secret).compact();
        headers.add(HttpHeaders.SET_COOKIE,token);

        Session session= new Session();
        session.setUser(check_user);
        session.setStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setExpiry(new Date(nowinms+1000000));
        session.setCreatedAt(new Date(nowinms));
        Session saved_session = sessionRepo.save(session);

        return new Pair<User,MultiValueMap<String,String>>(check_user,headers);

    }

    public Boolean validate(Long userid,String token){
        Optional<Session> fetch_session = sessionRepo.findByTokenAndUser_Id(token,userid);
        Optional<User> fetch_user = repo.findById(userid);
        if(fetch_session.isEmpty() || fetch_user.isEmpty()){
            System.out.println("user does not exist or session not found");
            return false;}

        //validate signature, claims, expiry raise exceptions
        Session session = fetch_session.get();
        String fetch_token = session.getToken();

        JwtParser parsed = Jwts.parser().verifyWith(secret).build();
        Claims claims = parsed.parseSignedClaims(fetch_token).getPayload();

        System.out.println(claims);

        long nowInMillis = System.currentTimeMillis();
        long tokenExpiry = (Long)claims.get("expiry");

        String email = fetch_user.get().getEmail();
        String claims_email = (String) claims.get("email");


        if(nowInMillis > tokenExpiry) {
            System.out.println(nowInMillis);
            System.out.println(tokenExpiry);
            System.out.println("Token has expired");
            return false;
        }
        if(!email.equals(claims_email)) {
            System.out.println(email);
            System.out.println(claims.get("email"));
            System.out.println("User doesn't match");
            return false;
        }

return true;
    }



}
