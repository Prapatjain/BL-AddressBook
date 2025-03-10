package com.example.Address.Book.services;


import com.example.Address.Book.dto.EmployeeDTO;
import com.example.Address.Book.dto.ResponseDTO;
import com.example.Address.Book.entities.EmployeeEntity;
import com.example.Address.Book.interfaces.IEmployeeService;
import com.example.Address.Book.repositories.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeService implements IEmployeeService {

    ObjectMapper obj = new ObjectMapper();

    @Autowired
    EmployeeRepository employeeRepository;

    public ResponseDTO response(String message, String status){
        return new ResponseDTO(message, status);
    }

    public EmployeeDTO get(Long id) throws Exception{

        EmployeeEntity foundEmp = employeeRepository.findById(id).orElseThrow(()->
        {
            log.error("Cannot find employee with id {}", id);
            return new RuntimeException("Cannot find employee with given id");
        });

        EmployeeDTO resDto = new EmployeeDTO(foundEmp.getName(), foundEmp.getEmail(), foundEmp.getId());

        log.info("Employee DTO send for id: {} is : {}", id, obj.writeValueAsString(resDto));

        return resDto;

    }

    public EmployeeDTO create(EmployeeDTO user) throws Exception{
        EmployeeEntity newUser = new EmployeeEntity(user.getName(), user.getEmail());

        employeeRepository.save(newUser);

        log.info("Employee saved in db: {}", obj.writeValueAsString(newUser));

        EmployeeDTO resDto = new EmployeeDTO(newUser.getName(), newUser.getEmail(), newUser.getId());

        log.info("Employee DTO sent: {}", obj.writeValueAsString(resDto));

        return resDto;
    }

    public List<EmployeeDTO> getAll(){

        return employeeRepository.findAll().stream().map(entity -> {
                                                                                  EmployeeDTO newUser = new EmployeeDTO(entity.getName(), entity.getEmail(), entity.getId());
                                                                                  return newUser;
        }).collect(Collectors.toList());

    }

    public EmployeeDTO edit(EmployeeDTO user, Long id) throws Exception{
        EmployeeEntity foundEmp = employeeRepository.findById(id).orElseThrow(()->{
            log.error("Cannot find employee with id : {}", id);
            return new RuntimeException("cannot find employee with given id");
        });

        foundEmp.setName(user.getName());
        foundEmp.setEmail(user.getEmail());

        employeeRepository.save(foundEmp);

        log.info("Employee saved after editing in db is : {}", obj.writeValueAsString(foundEmp));

        EmployeeDTO resDto = new EmployeeDTO(foundEmp.getName(), foundEmp.getEmail(), foundEmp.getId());

        return resDto;
    }

    public String delete(Long id){
        EmployeeEntity foundUser = employeeRepository.findById(id).orElseThrow(()->{
            log.error("Cannot find user with id : {}", id);
            return new RuntimeException("cannot find user with given id");
        });

        employeeRepository.delete(foundUser);

        return "employee deleted";
    }

    public String clear(){

        employeeRepository.deleteAll();
        return "db cleared";

    }





}
