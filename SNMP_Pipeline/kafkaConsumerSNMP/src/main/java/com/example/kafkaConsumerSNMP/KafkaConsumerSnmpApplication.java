package com.example.kafkaConsumerSNMP;

import com.example.kafkaConsumerSNMP.entities.EnrichedTrap;
import com.example.kafkaConsumerSNMP.entities.TrapData;
import com.example.kafkaConsumerSNMP.services.RethinkDBService;
import com.example.kafkaConsumerSNMP.services.Sender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@AllArgsConstructor
public class KafkaConsumerSnmpApplication {
	private final Sender sender;
	public static void main(String[] args) {
		SpringApplication.run(KafkaConsumerSnmpApplication.class, args);
	}
	public static int num = 0;
	@Autowired
	private RethinkDBService rethinkDBService;


	@KafkaListener(topics = "TRAP")
	public void handleKafkaMessage(String pduJson) {
		String json = pduJson;// Replace with your JSON string
		num++;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			TrapData t = objectMapper.readValue(json,TrapData.class);
			//Some Processing: Add this later
			EnrichedTrap t2 = new EnrichedTrap(t);
			//System.out.println("Not sent yet");
			sender.send(t2);
			//System.out.println("Sent now!");
			rethinkDBService.saveKafkaMessageToRethink(t2);
			System.out.println(num);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		//System.out.println(num);
	}
}
