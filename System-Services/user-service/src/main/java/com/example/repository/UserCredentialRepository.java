package com.example.repository;

import com.example.entity.UserCredential;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends CrudRepository<UserCredential,Integer> {
    Optional<UserCredential> findByUsername(String username);
    @Query("SELECT COUNT(u) FROM UserCredential u WHERE u.role = 'admin'")
    long countAdmins();

    @Query("SELECT COUNT(u) FROM UserCredential u WHERE u.role = 'user'")
    long countUsers();
}
