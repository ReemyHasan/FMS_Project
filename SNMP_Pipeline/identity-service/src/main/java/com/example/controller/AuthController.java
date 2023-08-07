package com.example.controller;

import com.example.dto.AuthRequest;
import com.example.entity.UserCredential;
import com.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        System.out.println("Hi" + authRequest);
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            System.out.println("HEY");
            if (authenticate.isAuthenticated()) {
                return service.generateToken(authRequest.getUsername());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Invalid Access";
        }

//        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//        System.out.println("HEY");
//        if (authenticate.isAuthenticated()) {
//            return service.generateToken(authRequest.getUsername());
//        } else {
//            System.out.println("invalid access");
//            throw new RuntimeException("invalid access");
//        }
        return null;
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {

       try {
           service.validateToken(token);
           return "Token is valid";
       }catch (Exception e){
           return "Token is not Valid";
       }
    }
}
