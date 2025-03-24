package com.example.Address.Book.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contacts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContactEntity {

    String name;
    String email;
    Long phoneNumber;
    String address;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    Long userId;

    public ContactEntity(String name, String email, Long phoneNumber, String address, Long userId) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.userId = userId;
    }
}
