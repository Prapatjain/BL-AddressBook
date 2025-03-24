package com.example.Address.Book.repositories;

import com.example.Address.Book.entities.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {

    public ContactEntity findByEmail(String email);

    public List<ContactEntity> findByUserId(Long userId);

}
