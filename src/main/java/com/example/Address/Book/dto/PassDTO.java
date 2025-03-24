package com.example.Address.Book.dto;

public class PassDTO {

    String password;

    public PassDTO(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

}
