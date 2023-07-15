package com.example.SnmpReciever;

import com.example.SnmpReciever.component.SnmpListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SnmpReceiverApplication implements CommandLineRunner {
	private final SnmpListener trapReceiver;

	public SnmpReceiverApplication(SnmpListener trapReceiver) {
		this.trapReceiver = trapReceiver;
	}
	public static void main(String[] args) {
		SpringApplication.run(SnmpReceiverApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		trapReceiver.listen();
	}
}