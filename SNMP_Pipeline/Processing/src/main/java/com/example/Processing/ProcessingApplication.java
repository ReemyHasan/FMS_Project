package com.example.Processing;

import com.example.Processing.entities.EnrichedTrap;
import com.example.Processing.entities.ProcessedTrap;
import com.example.Processing.entities.SeverityLevel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class ProcessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessingApplication.class, args);
	}
	public static int num = 0;

	@KafkaListener(topics = "EnrichedTrap")
	public void handleKafkaMessage(String pduJson) {
		String json = pduJson; // Replace with your JSON string
		num++;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			EnrichedTrap t = objectMapper.readValue(json,EnrichedTrap.class);
			//Some Processing: Add this later (Filtering, Prioritizing, Correlation)
			ProcessedTrap t2 = new ProcessedTrap(t);

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		//System.out.println(num);
	}
}
