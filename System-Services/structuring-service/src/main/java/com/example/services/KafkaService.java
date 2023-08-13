package com.example.services;

import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
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
