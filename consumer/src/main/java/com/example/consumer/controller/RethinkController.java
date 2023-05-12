package com.example.consumer.controller;

import com.example.consumer.services.RethinkDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"})
@RequestMapping("/api/rethink")
public class RethinkController {
    @Autowired
    private RethinkDBService rethinkDBService;


        @PostMapping("/data")
        public ResponseEntity<String> saveData(@RequestBody Map<String, Object> data) {
            rethinkDBService.saveData("my_database", "my_table", data);
            return ResponseEntity.ok("Data saved successfully in RethinkDB");
        }

        @GetMapping("/data")
        public ResponseEntity<List<Map<String, Object>>> getData() {
            List<Map<String, Object>> result = rethinkDBService.getData("my_database", "my_table");
            System.out.println(result);
            if (result != null) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    @KafkaListener(topics = "Test")
    public void handleKafkaMessage(String message) {
        rethinkDBService.saveKafkaMessageToRethink(message);
    }

}
