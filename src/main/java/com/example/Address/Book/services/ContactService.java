package com.example.Address.Book.services;


import com.example.Address.Book.dto.ContactDTO;
import com.example.Address.Book.dto.ResponseDTO;
import com.example.Address.Book.entities.ContactEntity;
import com.example.Address.Book.interfaces.IContactService;
import com.example.Address.Book.repositories.ContactRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.info.Contact;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContactService implements IContactService {

    @Autowired
    ContactRepository contactRepository;

    public ResponseDTO response(String message, String status){
        return new ResponseDTO(message, status);
    }

    public ContactDTO get(Long id){
        try {

            ContactEntity foundEmp = contactRepository.findById(id).orElseThrow(() ->
            {
                return new RuntimeException();
            });

            ContactDTO resDto = new ContactDTO(foundEmp.getName(), foundEmp.getEmail(), foundEmp.getId(), foundEmp.getAddress(), foundEmp.getId());

            log.info("Employee DTO send for id: {} is : {}", id, getJSON(resDto));

            return resDto;
        }
        catch(RuntimeException e){
            log.error("Cannot find employee with id {}", id);
        }
        return null;
    }

    public ContactDTO create(ContactDTO user){
        try {

            ContactEntity foundEntity = contactRepository.findByEmail(user.getEmail());

            if (foundEntity != null)
                throw new RuntimeException();

            ContactEntity newUser = new ContactEntity(user.getName(), user.getEmail(), user.getPhoneNumber(), user.getAddress());

            contactRepository.save(newUser);

            log.info("Employee saved in db: {}", getJSON(newUser));

            ContactDTO resDto = new ContactDTO(newUser.getName(), newUser.getEmail(), newUser.getPhoneNumber(), newUser.getAddress(), newUser.getId());

            log.info("Employee DTO sent: {}", getJSON(resDto));

            return resDto;
        }
        catch(RuntimeException e){
            log.error("Exception : {} Reason : {}", e, "User already created with given email");
        }
        return null;
    }

    public List<ContactDTO> getAll(){

        return contactRepository.findAll().stream().map(entity -> {
                                                                                  ContactDTO newUser = new ContactDTO(entity.getName(), entity.getEmail(), entity.getPhoneNumber(), entity.getAddress(), entity.getId());
                                                                                  return newUser;
        }).collect(Collectors.toList());

    }

    public ContactDTO edit(ContactDTO user, Long id) {
        ContactEntity foundEmp = contactRepository.findById(id).orElseThrow(()->{
            log.error("Cannot find employee with id : {}", id);
            return new RuntimeException("cannot find employee with given id");
        });

        foundEmp.setName(user.getName());
        foundEmp.setEmail(user.getEmail());

        contactRepository.save(foundEmp);

        log.info("Employee saved after editing in db is : {}", getJSON(foundEmp));

        ContactDTO resDto = new ContactDTO(foundEmp.getName(), foundEmp.getEmail(),foundEmp.getPhoneNumber(), foundEmp.getAddress(), foundEmp.getId());

        return resDto;
    }

    public String delete(Long id){
        ContactEntity foundUser = contactRepository.findById(id).orElseThrow(()->{
            log.error("Cannot find user with id : {}", id);
            return new RuntimeException("cannot find user with given id");
        });

        contactRepository.delete(foundUser);

        return "employee deleted";
    }

    public String clear(){

        contactRepository.deleteAll();
        return "db cleared";

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
