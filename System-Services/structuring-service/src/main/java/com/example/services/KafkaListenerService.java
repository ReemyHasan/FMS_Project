package com.example.services;

import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaListenerService {
    @Autowired
    TimeGrouping timeGrouping;

    @KafkaListener(topics = "Stream-Raw")
    public void handleKafkaMessage(String pduJson) {
        String json = pduJson;
        try {
            JSONObject jsonObject = new JSONObject(pduJson);
            timeGrouping.addRecord(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
