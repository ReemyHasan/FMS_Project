package com.example.WebApplication.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    public String severity;

    @JsonProperty("variableBindings")
    public List<VarBind> variableBindings = new ArrayList<VarBind>();

    public EnrichedTrap(TrapData pdu) {
        this.enterprise = pdu.getEnterprise().toString();
        this.genericTrap = pdu.getGenericTrap();
        this.specificTrap = pdu.getSpecificTrap();
        this.timestamp = pdu.getTimestamp();
        this.agentAddress = pdu.getAgentAddress().toString();
        this.variableBindings = pdu.getVariableBindings();

    }

}
