package com.example.WebApplication.controller;

import com.example.WebApplication.controller.SocketTextHandler;
import com.example.WebApplication.entities.RethinkChange;
import com.example.WebApplication.services.RethinkDBService;
import com.rethinkdb.net.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rethink")
@CrossOrigin(origins = {"http://localhost:3000"})
public class RethinkController {
    @Autowired
    private RethinkDBService rethinkDBService;
    @PostMapping("/delete")
    public void delete(@RequestBody String id){
        rethinkDBService.deleteById(id);
    }

    @GetMapping("/data")
    public ResponseEntity<List<Map<String, Object>>> getData() {
        List<Map<String, Object>> result = rethinkDBService.getData();
        System.out.println(result);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
