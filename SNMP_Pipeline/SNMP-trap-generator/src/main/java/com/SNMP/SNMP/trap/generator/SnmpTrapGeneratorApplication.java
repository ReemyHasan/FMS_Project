package com.SNMP.SNMP.trap.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.snmp4j.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.*;

import java.io.IOException;
import java.net.SocketException;

@SpringBootApplication
public class SnmpTrapGeneratorApplication {

	public static void main(String[] args) throws IOException {

		SpringApplication.run(SnmpTrapGeneratorApplication.class, args);
	}
}
