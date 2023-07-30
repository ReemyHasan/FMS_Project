package com.example.service;

import com.example.entity.UserCredential;
import com.example.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserCredentialRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public Optional<UserCredential> getUserByID(int id){
        return repository.findById(id);
    }
    public Optional<UserCredential> getUserByUsername(String username){
        return repository.findByUsername(username);
    }
    public boolean deleteUserByID(int id){
        try {
            repository.deleteById(id);
            return true;
        }
        catch (Exception e){
            System.out.println("error in deleting user");
            return false;
        }

    }
}
