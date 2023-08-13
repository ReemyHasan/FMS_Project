package com.example.controllers;

import com.example.services.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stream/predict")
@RequiredArgsConstructor
public class PredictController {
    @Autowired
    PredictionService predictionService;
    @PostMapping("/instance")
    public String predict(@RequestBody String jsonObject) {
        System.out.println(jsonObject);
        String s = predictionService.getPrediction(jsonObject);
        return s;
    }
}
