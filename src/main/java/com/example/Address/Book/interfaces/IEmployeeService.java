package com.example.Address.Book.interfaces;

import java.util.*;
import com.example.Address.Book.dto.EmployeeDTO;
import com.example.Address.Book.dto.ResponseDTO;
import com.example.Address.Book.entities.EmployeeEntity;
import org.springframework.stereotype.Service;

@Service
public interface IEmployeeService {



    public EmployeeDTO get(Long id);

    public EmployeeDTO create(EmployeeDTO user);

    public String clear();

    public List<EmployeeDTO> getAll();

    public EmployeeDTO edit(EmployeeDTO user, Long id);

    public String delete(Long id);

    public ResponseDTO response(String message, String status);
}
