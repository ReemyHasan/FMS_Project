package com.example.services;

import com.example.entities.EnrichedTrap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class Sender {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(EnrichedTrap enrichedTrap){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String pduJson = objectMapper.writeValueAsString(enrichedTrap);
            kafkaTemplate.send("EnrichedTrap", pduJson);
        } catch (JsonProcessingException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
