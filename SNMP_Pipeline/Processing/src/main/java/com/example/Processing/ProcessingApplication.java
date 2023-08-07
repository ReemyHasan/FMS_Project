package com.example.Processing;

import com.example.Processing.entities.EnrichedTrap;
import com.example.Processing.entities.ProcessedTrap;
import com.example.Processing.entities.SeverityLevel;
import com.example.Processing.services.ElasticService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
@EnableDiscoveryClient
public class ProcessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessingApplication.class, args);

	}
	public static int num = 0;
	@Autowired
	private ElasticService elasticService;
	@KafkaListener(topics = "EnrichedTrap")
	public void handleKafkaMessage(String pduJson) {
		String json = pduJson; // Replace with your JSON string
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
		//System.out.println(num);
	}
	@PostConstruct
	public void magic(){
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				// Call your function here
				myFunction();
			}
		};
		// Schedule the task to run every 10 seconds
		timer.schedule(task, 0, 3000);
	}
	public void myFunction(){
		System.out.println("Hey");
		elasticService.sendBulk();
	}
}
