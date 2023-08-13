package com.example.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
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
        if (this.variableBindings.size() > 5){
            this.severity = SeverityLevel.WARNING;
        }
        else if (this.variableBindings.size() >= 3){
            this.severity = SeverityLevel.ERROR;
        }
        else this.severity = SeverityLevel.INFO;

    }
}
