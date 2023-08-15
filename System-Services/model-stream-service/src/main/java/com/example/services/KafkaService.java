package com.example.services;

import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class KafkaService {
    @Autowired
    private ElasticService elasticService;
    @Autowired
    PredictionService predictionService;
    public static int num = 0;
    @KafkaListener(topics = "Stream-Instance")
    public void handleKafkaMessage(String pduJson) {
        String json = pduJson; // Replace with your JSON string
        num++;
        try {
            JSONObject jsonObject = new JSONObject(pduJson);
            String s = predictionService.getPrediction(pduJson);
            if (s.charAt(0) != 'A' && s.charAt(0) != 'N' && s.charAt(0) != 'D'){
                return;
            }
            jsonObject.put("class",s);
            jsonObject.put("@timestamp",jsonObject.get("timestamp"));
            elasticService.addToBulk(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
