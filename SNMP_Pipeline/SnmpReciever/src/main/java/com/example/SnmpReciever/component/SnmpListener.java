package com.example.SnmpReciever.component;
import com.example.SnmpReciever.enitites.TrapData;
//import com.example.SnmpReciever.services.KafkaService;
import com.example.SnmpReciever.services.KafkaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.snmp4j.*;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.TransportIpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*@RestController
@RequestMapping("/api/product")*/
@Component
public class SnmpListener implements CommandResponder {

    @Autowired
    private KafkaService kafkaService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${spring.listener.address}")
    private String addressString;

    @Value("${spring.listener.threads}")
    private Integer threadsNum;

    @Value("${spring.listener.community}")
    private String community;


    @Override
    public synchronized void processPdu(CommandResponderEvent event) {
        System.out.println("Received PDU...");
        PDU x = event.getPDU();
        PDUv1 pdu = (PDUv1) x;
        TrapData trapData = new TrapData(pdu);
        if (pdu != null) {
            try {
                String pduJson = objectMapper.writeValueAsString(trapData);
                kafkaService.send("TRAP", pduJson);
            } catch (JsonProcessingException e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
        }
    }
    public void startTrapListener() {
        try {
            TransportMapping<?> transport = new DefaultUdpTransportMapping(new UdpAddress(addressString));
            System.out.println("Listening to SNMP Trap");
            Snmp snmp = new Snmp(transport);
            snmp.addCommandResponder(this);
            transport.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized void listen()
            throws IOException {
        AbstractTransportMapping transport;
        TransportIpAddress address = new UdpAddress(addressString);
        if (address instanceof TcpAddress) {
            transport = new DefaultTcpTransportMapping((TcpAddress) address);
        } else {
            transport = new DefaultUdpTransportMapping((UdpAddress) address);
        }

        ThreadPool threadPool = ThreadPool.create("DispatcherPool", threadsNum);
        MessageDispatcher mDispatcher = new MultiThreadedMessageDispatcher(
                threadPool, new MessageDispatcherImpl());

        // add message processing models
        mDispatcher.addMessageProcessingModel(new MPv1());
        mDispatcher.addMessageProcessingModel(new MPv2c());

        // add all security protocols
        SecurityProtocols.getInstance().addDefaultProtocols();
        SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());

        // Create Target
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));

        Snmp snmp = new Snmp(mDispatcher, transport);
        snmp.addCommandResponder(this);

        transport.listen();
        System.out.println("Listening on " + address);
        try {
            this.wait();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
