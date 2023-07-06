package com.example.SnmpReciever;
import com.example.SnmpReciever.enitites.TrapData;
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
        PDUv1 pdu = (PDUv1) event.getPDU();
        TrapData trapData = new TrapData(pdu);
        if (pdu != null) {
            try {
                String pduJson = objectMapper.writeValueAsString(trapData);
                kafkaTemplate.send("TRAP", pduJson);
            } catch (JsonProcessingException e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
        }
    }
    public void startTrapListener() {
        try {
            TransportMapping<?> transport = new DefaultUdpTransportMapping(new UdpAddress("localhost/1625"));
            System.out.println("Listening to SNMP Trap");
            Snmp snmp = new Snmp(transport);
            snmp.addCommandResponder(this);
            transport.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
