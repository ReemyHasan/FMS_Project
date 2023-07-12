//package com.example.consumer.controller;
//
//import com.example.consumer.entity.Trap;
//import com.example.consumer.services.TrapService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@CrossOrigin(origins = {"http://localhost:5173"})
//@RequestMapping("/api/elastic")
//public class TrapController {
//
//    @Autowired
//    private TrapService trapService;
//    @GetMapping("/findAllTraps")
//    Iterable<Trap> findAll(){
//        return trapService.getTraps();
//
//    }
//
//    @PostMapping("/insertTraps")
//    public Trap insertTrap(@RequestBody  Trap trap){
//        return trapService.insertTrap(trap);
//    }
//    @KafkaListener(topics = "Test")
//    public void handleKafkaMessage(String message) {
//        trapService.saveKafkaMessageToElastic(message);
//    }
//
//}
//
