package com.example.repository;

import com.example.entity.UserCredential;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepository  extends CrudRepository<UserCredential,Integer> {
    Optional<UserCredential> findByUsername(String username);
}
