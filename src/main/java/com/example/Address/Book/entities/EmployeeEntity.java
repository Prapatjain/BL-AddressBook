package com.example.Address.Book.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employees")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeEntity {

    String name;
    String email;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    public EmployeeEntity(String name, String email) {
        this.name = name;
        this.email = email;

    }
}
