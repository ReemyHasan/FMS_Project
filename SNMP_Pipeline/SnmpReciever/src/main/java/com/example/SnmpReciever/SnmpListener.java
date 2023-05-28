package com.example.SnmpReciever;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.snmp4j.*;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*@RestController
@RequestMapping("/api/product")*/
@Component
public class SnmpListener implements CommandResponder {


    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    public SnmpListener(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public synchronized void processPdu(CommandResponderEvent event) {
        System.out.println("Received PDU...");
        PDU pdu = event.getPDU();
        if (pdu != null) {
            try {
                String pduJson = objectMapper.writeValueAsString(pdu);
                kafkaTemplate.send("TRAP", pduJson);
                System.out.println(" Hello World"+ pduJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void startTrapListener() {
        try {
            System.out.println("Listening to SNMP Trap");
            TransportMapping<?> transport = new DefaultUdpTransportMapping(new UdpAddress("localhost/162"));
            Snmp snmp = new Snmp(transport);
            snmp.addCommandResponder(this);
            transport.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
