package com.example.services;

import com.example.entities.EnrichedTrap;
import com.example.entities.ProcessedTrap;
import com.example.entities.TrapData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaListenerService {
    private static int num = 0;
    @Autowired
    private ElasticService elasticService;
    @KafkaListener(topics = "EnrichedTrap")
    public void handleKafkaMessage(String pduJson) {
        String json = pduJson;
        num++;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            EnrichedTrap t = objectMapper.readValue(json,EnrichedTrap.class);
            //Some Processing: Add this later (Filtering, Prioritizing, Correlation)
            ProcessedTrap t2 = new ProcessedTrap(t);
            elasticService.addToBulk(t2);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        System.out.println(num);
    }

}
