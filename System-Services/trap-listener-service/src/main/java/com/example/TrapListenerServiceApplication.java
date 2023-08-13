package com.example;

import com.example.component.SnmpListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrapListenerServiceApplication implements CommandLineRunner {
	private final SnmpListener trapReceiver;

	public TrapListenerServiceApplication(SnmpListener trapReceiver) {
		this.trapReceiver = trapReceiver;
	}
	public static void main(String[] args) {
		SpringApplication.run(TrapListenerServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		trapReceiver.listen();
	}
}