package com.example.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args) {

		SpringApplication.run(ConsumerApplication.class, args);
	}
//	@KafkaListener(topics = "Test")
//	public void handleNotification(String s) {
//
//		System.out.println("Received Message from Producer: "+s+"\n");
//	}

}
