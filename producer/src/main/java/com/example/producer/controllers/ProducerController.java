package com.example.producer.controllers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProducerController {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProducerController(KafkaTemplate<String, String> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping
    public String p(){
        for (int i = 0; i < 10; i++) {
            kafkaTemplate.send("Test", "SnmpTrap");
            //System.out.println("Hello");
        }
        return "Trap send successfully";
    }
}
