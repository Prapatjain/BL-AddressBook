package com.example.Address.Book.interfaces;

import com.example.Address.Book.dto.AuthUserDTO;
import com.example.Address.Book.dto.LoginDTO;
import com.example.Address.Book.dto.PassDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface IAuthInterface {

    public String register (AuthUserDTO user);


    public String login(LoginDTO user, HttpServletResponse response);

    public AuthUserDTO forgotPassword(PassDTO pass, String email);

    public String resetPassword(String email, String currentPass, String newPass);

    public String clear();

}
