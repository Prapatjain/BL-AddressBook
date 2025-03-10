package com.example.Address.Book.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class AuthUser {

    String firstName;
    String lastName;
    String email;
    String password;
    String hashPass;
    String token;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    public AuthUser() {
    }

    public AuthUser(String firstName, String lastName, String email, String password, String hashPass) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.hashPass = hashPass;

        this.token="";
        this.id = null;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }
    public void setHashPass(String hashPass) {
        this.hashPass = hashPass;
    }

    public String getHashPass() {
        return hashPass;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
