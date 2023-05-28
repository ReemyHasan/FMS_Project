package com.example.SnmpReciever;

import org.snmp4j.PDU;
import org.snmp4j.smi.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

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
		trapReceiver.startTrapListener();

	}
}