package com.example.userManagement.controller;

import com.example.userManagement.entity.User;
import com.example.userManagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
//@CrossOrigin(origins = {"*"})
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public Iterable<User> findAll() {
        return userService.getUsers();

    }

    @GetMapping("/Users/{id}")
    public User findById(@PathVariable int id) {
        return userService.getUserById(id);

    }

    @PostMapping("/insertUser")
    public User insertUser(@RequestBody final User user) {
        return userService.insertUser(user);
    }

    @PutMapping("/updateUser/{id}")
    public User updateUser(@RequestBody User user, @PathVariable int id) {
        return userService.updateUser(user, id);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }


}
