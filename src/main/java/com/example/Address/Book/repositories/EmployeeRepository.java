package com.example.Address.Book.repositories;

import com.example.Address.Book.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    public EmployeeEntity findByEmail(String email);

}
