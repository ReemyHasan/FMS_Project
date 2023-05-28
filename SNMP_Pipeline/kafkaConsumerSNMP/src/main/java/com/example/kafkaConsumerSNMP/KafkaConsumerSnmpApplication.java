package com.example.kafkaConsumerSNMP;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class KafkaConsumerSnmpApplication {
	public static void main(String[] args) {
		SpringApplication.run(KafkaConsumerSnmpApplication.class, args);
	}
	@KafkaListener(topics = "TRAP")
	public void handleKafkaMessage(String pduJson) {
		String json = pduJson; // Replace with your JSON string
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(json);
			System.out.println(jsonNode);
			String value = jsonNode.get("variableBindings").asText();
			System.out.println("Value: " + value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
