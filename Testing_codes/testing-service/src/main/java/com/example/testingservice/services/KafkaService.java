package com.example.testingservice.services;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic,String message){
        kafkaTemplate.send(topic,message);
    }
}
