package com.example.userService.service;

import com.example.userService.entity.UserCredential;
import com.example.userService.repository.UserCredentialRepository;
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

    public Optional<UserCredential> getUserByID(int id){
        return repository.findById(id);
    }
    public Optional<UserCredential> getUserByUsername(String username){
        return repository.findByUsername(username);
    }

    public Optional<UserCredential> saveUser(UserCredential credential) {
//        System.out.println("dskmdelkwmelkamdk     "+credential);
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        try {
            repository.save(credential);
            return repository.findByUsername(credential.getUsername());
        }
        catch (Exception e){
            System.out.println(e);
            return Optional.of(credential);
        }
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

    public Iterable<UserCredential> getAllUsers(){
        try {
            return repository.findAll();

        }
        catch (Exception e){
            System.out.println("error in  get All users");
            return null;
        }

    }
    public boolean updateUser(UserCredential updatedUser) {
        try {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            repository.save(updatedUser);
            return true;
        } catch (Exception e) {
            System.out.println("Error in updating user");
            return false;
        }
    }
}
