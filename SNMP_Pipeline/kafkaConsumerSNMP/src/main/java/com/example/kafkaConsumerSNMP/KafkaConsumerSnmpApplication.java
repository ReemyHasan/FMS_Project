package com.example.kafkaConsumerSNMP;

import com.example.kafkaConsumerSNMP.entities.TrapData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class KafkaConsumerSnmpApplication {
	public static int num = 0;
	public static void main(String[] args) {
		SpringApplication.run(KafkaConsumerSnmpApplication.class, args);
	}
	@KafkaListener(topics = "TRAP")
	public void handleKafkaMessage(String pduJson) {
		String json = pduJson; // Replace with your JSON string
		num++;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			TrapData t = objectMapper.readValue(json,TrapData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(num);
	}
}
