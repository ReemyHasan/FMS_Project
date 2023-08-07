package com.example.userService.controllers;


import com.example.userService.entity.UserCredential;
import com.example.userService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/get/{username}")
    public Optional<UserCredential> getByUsername(@PathVariable String username) {

        try {
             System.out.println(userService.getUserByUsername(username));
           return userService.getUserByUsername(username);
        }catch (Exception e){
            return null;
        }
    }
    @DeleteMapping("/delete/{id}")
    public boolean deleteUser(@PathVariable int id) {

        try {
//            System.out.println(userService.getUserByUsername(username));
            return userService.deleteUserByID(id);
        }catch (Exception e){
            return false;
        }
    }
    @PostMapping("/register")
    public Optional<UserCredential> addNewUser(@RequestBody UserCredential user) {
        return userService.saveUser(user);
    }

    @GetMapping("/getAllUsers")
    public Iterable<UserCredential> getAllUsers() {

        try {
            return userService.getAllUsers();
        }catch (Exception e){
            return null;
        }
    }

    @PutMapping("/updateUser")
    public boolean updateUser(@RequestBody UserCredential userCredential) {

        try {
            return userService.updateUser(userCredential);
        }catch (Exception e){
            return false;
        }
    }
}

