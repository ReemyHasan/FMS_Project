package com.example.userService.repository;

import com.example.userService.entity.UserCredential;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends CrudRepository<UserCredential,Integer> {
    Optional<UserCredential> findByUsername(String username);
}
