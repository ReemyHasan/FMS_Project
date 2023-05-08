package com.example.consumer.controller;

import com.example.consumer.repository.Trap;
import com.example.consumer.services.ElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"})
@RequestMapping("/api/elastic")
public class ElasticController {

    @Autowired
    private ElasticService elasticService;
    @GetMapping("/findAllTraps")
    Iterable<Trap> findAll(){
        return elasticService.getTraps();

    }

    @PostMapping("/insertTraps")
    public Trap insertProduct(@RequestBody  Trap trap){
        return elasticService.insertTrap(trap);
    }
    @KafkaListener(topics = "Test")
    public void handleKafkaMessage(String message) {
        elasticService.saveKafkaMessageToElastic(message);
    }

}

