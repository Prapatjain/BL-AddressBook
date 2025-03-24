package com.example.Address.Book.services;

import com.example.Address.Book.dto.AuthUserDTO;
import com.example.Address.Book.dto.LoginDTO;
import com.example.Address.Book.dto.PassDTO;
import com.example.Address.Book.interfaces.IAuthInterface;
import com.example.Address.Book.entities.AuthUser;
import com.example.Address.Book.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthenticationService implements IAuthInterface {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageProducer messageProducer;    //RabbitMq Message Producer

    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    RedisTokenService redisTokenService;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public String register(AuthUserDTO user) {
        try {
            List<AuthUser> l1 = userRepository.findAll().stream().filter(authuser -> user.getEmail().equals(authuser.getEmail())).collect(Collectors.toList());

            if (l1.size() > 0) {
                throw new RuntimeException();
            }

            //creating hashed password using bcrypt
            String hashPass = bCryptPasswordEncoder.encode(user.getPassword());

            //creating new user
            AuthUser newUser = new AuthUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), hashPass);

            //setting the new hashed password
            newUser.setHashPass(hashPass);

            //saving the user in the database
            userRepository.save(newUser);

            log.info("User saved in database : {}", getJSON(newUser));

            //sending the custom message to Message Producer( Rabbit MQ)
            String customMessage = "REGISTER|"+user.getEmail()+"|"+user.getFirstName();
            messageProducer.sendMessage(customMessage);

            return "user registered";
        }
        catch(RuntimeException e){
            log.error("User already registered with email: {} Exception : {}", user.getEmail(), e);
        }
        return null;
    }


    public String login(LoginDTO user, HttpServletResponse response){
        try {
            List<AuthUser> l1 = userRepository.findAll().stream().filter(authuser -> authuser.getEmail().equals(user.getEmail())).collect(Collectors.toList());
            if (l1.size() == 0) {
                throw new RuntimeException();
            }
            AuthUser foundUser = l1.get(0);

            //matching the stored hashed password with the password provided by user
            if (!bCryptPasswordEncoder.matches(user.getPassword(), foundUser.getHashPass())) {
                log.error("Invalid password entered for email {} where entered password is {}", user.getEmail(), user.getPassword());
                return "Invalid password";
            }

            //creating Jwt Token
            String token = jwtTokenService.createToken(foundUser.getId());

            //store the token generated in cookies
            ResponseCookie resCookie = ResponseCookie.from("jwt", token)
            .httpOnly(true)
            .secure(false)      //set to true but for local host set it to false as local host sent uses HTTP request
            .path("/")
            .maxAge(3600)
            .sameSite("Strict")
            .build();

            response.addHeader(HttpHeaders.SET_COOKIE, resCookie.toString());

            //store the token in redis server as well
            redisTokenService.saveToken(foundUser.getId().toString(), token);   //(key:useId, value: token)

            //setting token for user login
            foundUser.setToken(token);

            //saving the current status of user in database
            userRepository.save(foundUser);

            log.info("User logged in with email {}", user.getEmail());

            return "user logged in" + "\ntoken : " + token;
        }
        catch(RuntimeException e){
            log.error("User not registered with email: {} Exception : {}", user.getEmail(), e);
        }
        return null;

    }

    public AuthUserDTO forgotPassword(PassDTO pass, String email){
        try {
            AuthUser foundUser = userRepository.findByEmail(email);

            if (foundUser == null) {
                throw new RuntimeException();
            }
            String hashpass = bCryptPasswordEncoder.encode(pass.getPassword());

            foundUser.setPassword(pass.getPassword());
            foundUser.setHashPass(hashpass);

            log.info("Hashpassword : {} for password : {} saved for user: {}", hashpass, pass.getPassword(),getJSON(foundUser));

            userRepository.save(foundUser);

            //sending the custom message to Message Producer
            String customMessage = "FORGOT|"+foundUser.getEmail()+"|"+foundUser.getFirstName();
            messageProducer.sendMessage(customMessage);

            AuthUserDTO resDto = new AuthUserDTO(foundUser.getFirstName(), foundUser.getLastName(), foundUser.getEmail(), foundUser.getPassword(), foundUser.getId());

            return resDto;
        }
        catch(RuntimeException e){
            log.error("user not registered with email: {} Exception : {}", email, e);
        }
        return null;
    }

    public String resetPassword(String email, String currentPass, String newPass) {

        AuthUser foundUser = userRepository.findByEmail(email);
        if (foundUser == null)
            return "user not registered!";

        if (!bCryptPasswordEncoder.matches(currentPass, foundUser.getHashPass()))
            return "incorrect password!";

        String hashpass = bCryptPasswordEncoder.encode(newPass);

        foundUser.setHashPass(hashpass);
        foundUser.setPassword(newPass);

        userRepository.save(foundUser);

        log.info("Hashpassword : {} for password : {} saved for user : {}", hashpass, newPass, getJSON(foundUser));

        String customMessage = "RESET|"+foundUser.getEmail()+"|"+foundUser.getFirstName();
        messageProducer.sendMessage(customMessage);

        return "Password reset successfull!";

    }

    public String logout(HttpServletRequest request, HttpServletResponse response){

        Cookie foundCookie = null;

        if(request.getCookies() ==  null)
            return "user not logged in";

        for(Cookie c : request.getCookies()){
            if(c.getName().equals("jwt")){
                foundCookie = c;
                break;
            }
        }
        if(foundCookie == null)
            return "user not logged in";

        ResponseCookie expiredCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());

        redisTokenService.deleteToken(jwtTokenService.decodeToken(foundCookie.getValue()).toString());

        return "You are logged out";
    }

    public String clear(){

        userRepository.deleteAll();
        log.info("all data inside db is deleted");

        return "Database cleared";
    }


    public String getJSON(Object object){
        try {
            ObjectMapper obj = new ObjectMapper();
            return obj.writeValueAsString(object);
        }
        catch(JsonProcessingException e){
            log.error("Reason : {} Exception : {}", "Conversion error from Java Object to JSON");
        }
        return null;
    }


}