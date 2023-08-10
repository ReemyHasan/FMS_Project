package com.example.kafkaConsumerSNMP.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.snmp4j.PDUv1;
import org.snmp4j.smi.VariableBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Setter
@Getter
public class EnrichedTrap {
    @JsonProperty("enterprise")
    public String enterprise;
    @JsonProperty("agentAddress")
    public String agentAddress;
    @JsonProperty("genericTrap")
    public int genericTrap;
    @JsonProperty("specificTrap")
    public int specificTrap;
    @JsonProperty("timestamp")
    public long timestamp;

    @JsonProperty("date")
    public long date;
    @JsonProperty("severity")
    public SeverityLevel severity;

    @JsonProperty("variableBindings")
    public List<VarBind> variableBindings = new ArrayList<VarBind>();

    public EnrichedTrap(TrapData pdu) {
        this.enterprise = pdu.getEnterprise().toString();
        this.genericTrap = pdu.getGenericTrap();
        this.specificTrap = pdu.getSpecificTrap();
        this.timestamp = pdu.getTimestamp();
        this.agentAddress = pdu.getAgentAddress().toString();
        this.variableBindings = pdu.getVariableBindings();
        this.date = new Date().getTime();

        int[] choices = {1, 2, 3};

        Random random = new Random();
        int randomIndex = random.nextInt(choices.length);

        int randomChoice = choices[randomIndex];

        if (randomChoice == 1){
            this.severity = SeverityLevel.WARNING;
        }
        else if (randomChoice == 2){
            this.severity = SeverityLevel.ERROR;
        }
        else this.severity = SeverityLevel.INFO;

    }
}
