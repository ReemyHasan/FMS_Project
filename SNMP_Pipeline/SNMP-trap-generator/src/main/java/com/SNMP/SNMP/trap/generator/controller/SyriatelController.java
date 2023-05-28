package com.SNMP.SNMP.trap.generator.controller;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/send")
public class SyriatelController {

    @GetMapping
    public void p() throws IOException {

        // Create a transport mapping
        TransportMapping<?> transport = new DefaultUdpTransportMapping();

        // Create an SNMP object
        Snmp snmp = new Snmp(transport);

        // Set the community string
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));

        // Set the IP address and port of the trap receiver
        target.setAddress(new UdpAddress("localhost/162"));

        // Set the SNMP version and other parameters
        target.setVersion(SnmpConstants.version2c);
        target.setTimeout(5000);
        target.setRetries(3);

        for (int i = 0; i < 1; i++) {
            OID trapOID = new OID("1.3.6.1.4.1.9999.2.0");
            PDU pdu = new PDU();
            pdu.setType(PDU.TRAP);
            pdu.add(new VariableBinding(SnmpConstants.sysObjectID, new TimeTicks(0)));
            pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, trapOID));
            pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress, new IpAddress("localhost")));
            pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.9999.3.0"), new OctetString("hello world" + i)));
            PDU pdu1 = new PDU();
            pdu1.setType(PDU.TRAP);
            pdu1.add(new VariableBinding(new OID("1.3.6.1.2.1.1.3.0"), new TimeTicks(0)));
            pdu1.add(new VariableBinding(SnmpConstants.snmpTrapOID, trapOID));
            pdu1.add(new VariableBinding(SnmpConstants.snmpTrapAddress, new IpAddress("localhost")));
            pdu1.add(new VariableBinding(new OID("1.3.6.1.4.1.9999.3.0"), new OctetString("System-wide failure" + i)));
            snmp.send(pdu, target);
            snmp.send(pdu1, target);
        }
        snmp.close();
        transport.close();
    }
}

