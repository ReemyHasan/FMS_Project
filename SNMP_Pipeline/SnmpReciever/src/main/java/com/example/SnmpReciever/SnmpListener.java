package com.example.SnmpReciever;
import com.example.SnmpReciever.enitites.TrapData;
//import com.example.SnmpReciever.services.KafkaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.snmp4j.*;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*@RestController
@RequestMapping("/api/product")*/
@Component
@AllArgsConstructor
public class SnmpListener implements CommandResponder {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    @Override
    public synchronized void processPdu(CommandResponderEvent event) {
        System.out.println("Received PDU...");
        PDU x = event.getPDU();
//        System.out.println(x);
//        System.out.println(x.getType());
//
        PDUv1 pdu = (PDUv1) x;
        TrapData trapData = new TrapData(pdu);
        if (pdu != null) {
            try {
                String pduJson = objectMapper.writeValueAsString(trapData);
//                System.out.println(pduJson);
                kafkaTemplate.send("TRAP", pduJson);
            } catch (JsonProcessingException e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
        }
    }
    public void startTrapListener() {
        try {
            TransportMapping<?> transport = new DefaultUdpTransportMapping(new UdpAddress("192.168.26.46/1625"));
            System.out.println("Listening to SNMP Trap");
            Snmp snmp = new Snmp(transport);
            snmp.addCommandResponder(this);
            transport.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
