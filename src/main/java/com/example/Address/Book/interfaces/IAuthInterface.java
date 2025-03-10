package com.example.Address.Book.interfaces;

import com.example.Address.Book.dto.AuthUserDTO;
import com.example.Address.Book.dto.LoginDTO;
import com.example.Address.Book.dto.PassDTO;
import org.springframework.stereotype.Service;

@Service
public interface IAuthInterface {

    public String register (AuthUserDTO user) throws Exception;


    public String login(LoginDTO user);

    public AuthUserDTO forgotPassword(PassDTO pass, String email) throws Exception;

    public String resetPassword(String email, String currentPass, String newPass) throws Exception;
}
