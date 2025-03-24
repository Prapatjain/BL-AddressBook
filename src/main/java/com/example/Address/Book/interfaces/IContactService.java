package com.example.Address.Book.interfaces;

import java.util.*;
import com.example.Address.Book.dto.ContactDTO;
import com.example.Address.Book.dto.ResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface IContactService {



    public ContactDTO get(Long id, HttpServletRequest request);

    public ContactDTO create(ContactDTO user, HttpServletRequest request);

    public String clear();

    public List<ContactDTO> getAll(HttpServletRequest request);

    public ContactDTO edit(ContactDTO user, Long id, HttpServletRequest request);

    public String delete(Long id, HttpServletRequest request);

    public ResponseDTO response(String message, String status);
}
