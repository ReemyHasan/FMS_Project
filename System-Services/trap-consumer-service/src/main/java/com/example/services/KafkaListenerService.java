package com.example.services;

import com.example.entities.EnrichedTrap;
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
    private Sender sender;
    @Autowired
    private RethinkDBService rethinkDBService;


    @KafkaListener(topics = "TRAP")
    public void handleKafkaMessage(String pduJson) {
        String json = pduJson;
        num++;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TrapData t = objectMapper.readValue(json,TrapData.class);
            //Some Processing: Add this later
            EnrichedTrap t2 = new EnrichedTrap(t);
            sender.send(t2);
            rethinkDBService.saveKafkaMessageToRethink(t2);
            System.out.println(num);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

}
