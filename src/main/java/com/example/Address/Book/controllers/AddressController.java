package com.example.Address.Book.controllers;

import java.util.*;
import com.example.Address.Book.dto.EmployeeDTO;
import com.example.Address.Book.dto.ResponseDTO;
import com.example.Address.Book.interfaces.IEmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addressbook")
@Slf4j
public class AddressController {

    ObjectMapper obj = new ObjectMapper();

    @Autowired
    IEmployeeService iEmployeeService;

    //UC1 --> REST API's handled using responseDTO(without interference of service layer)

    @GetMapping("/res/get/{id}")
    public ResponseDTO get1(@PathVariable Long id){

        log.info("Employee tried to get with id: {}", id);

        return new ResponseDTO("API triggered at /res/get/{id}", "Success");
    }

    @PostMapping("/res/create")
    public ResponseDTO create1(@RequestBody EmployeeDTO user) throws Exception{

        log.info("Employee tried to create with body: {}", obj.writeValueAsString(user));

        return new ResponseDTO("API triggered at /res/create", "Success");
    }

    @GetMapping("/res/getAll")
    public ResponseDTO getAll1(){

        log.info("Employee tried to getAll");

        return new ResponseDTO("API triggered at /res/getAll", "Success");
    }

    @PutMapping("/res/edit/{id}")
    public ResponseDTO edit1(@RequestBody EmployeeDTO user, @PathVariable Long id) throws Exception{

        log.info("Employee tried to edit with id : {} and body : {}", id, obj.writeValueAsString(user));

        return new ResponseDTO("API triggered at /res/edit/{id}", "Success");
    }

    @DeleteMapping("/res/delete/{id}")
    public ResponseDTO delete1(@PathVariable Long id){

        log.info("Employee tried to delete with id: {}", id);

        return new ResponseDTO("API triggered at /res/delete/{id}", "Success");
    }

    //UC2 --> Handling REST API's using Service layer without storing in DB

    @GetMapping("/res2/get/{id}")
    public ResponseDTO get2(@PathVariable Long id){

        log.info("Employee tried to get with id: {}", id);

        return iEmployeeService.response("API triggered at /res/get/{id}", "Success");
    }

    @PostMapping("/res2/create")
    public ResponseDTO create2(@RequestBody EmployeeDTO user) throws Exception{

        log.info("Employee tried to create with body: {}", obj.writeValueAsString(user));

        return iEmployeeService.response("API triggered at /res/create", "Success");
    }

    @GetMapping("/res2/getAll")
    public ResponseDTO getAll2(){

        log.info("Employee tried to getAll");

        return iEmployeeService.response("API triggered at /res/getAll", "Success");
    }

    @PutMapping("/res2/edit/{id}")
    public ResponseDTO edit2(@RequestBody EmployeeDTO user, @PathVariable Long id) throws Exception{

        log.info("Employee tried to edit with id : {} and body : {}", id, obj.writeValueAsString(user));

        return iEmployeeService.response("API triggered at /res/edit/{id}", "Success");
    }

    @DeleteMapping("/res2/delete/{id}")
    public ResponseDTO delete2(@PathVariable Long id){

        log.info("Employee tried to delete with id: {}", id);

        return iEmployeeService.response("API triggered at /res/delete/{id}", "Success");
    }

    //UC3 --> Handling REST API's using service layer with storage in database

    @GetMapping("/get/{id}")
    public EmployeeDTO get3(@PathVariable Long id) throws Exception{

        log.info("Employee tried to get with id: {}", id);

        return iEmployeeService.get(id);
    }

    @PostMapping("/create")
    public EmployeeDTO create3(@RequestBody EmployeeDTO user) throws Exception{

        log.info("Employee tried to create with body: {}", obj.writeValueAsString(user));

        return iEmployeeService.create(user);
    }

    @GetMapping("/getAll")
    public List<EmployeeDTO> getAll3(){

        log.info("Employee tried to getAll");

        return iEmployeeService.getAll();
    }

    @PutMapping("/edit/{id}")
    public EmployeeDTO edit3(@RequestBody EmployeeDTO user, @PathVariable Long id) throws Exception{

        log.info("Employee tried to edit with id : {} and body : {}", id, obj.writeValueAsString(user));

        return iEmployeeService.edit(user, id);
    }

    @DeleteMapping("/delete/{id}")
    public String delete3(@PathVariable Long id){

        log.info("Employee tried to delete with id: {}", id);

        return iEmployeeService.delete(id);
    }

    @GetMapping("/clear")
    public String clear(){
        return iEmployeeService.clear();
    }


}
