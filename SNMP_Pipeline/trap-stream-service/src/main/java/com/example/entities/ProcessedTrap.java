package com.example.Processing.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ProcessedTrap {
    @JsonProperty("enterprise")
    public String enterprise;
    @JsonProperty("agentAddress")
    public String agentAddress;
    @JsonProperty("genericTrap")
    public int genericTrap;
    @JsonProperty("specificTrap")
    public int specificTrap;
    @JsonProperty("@timestamp")
    public long timestamp;

    @JsonProperty("severity")
    public SeverityLevel severity;

    @JsonProperty("variableBindings")
    public List<VarBind> variableBindings = new ArrayList<VarBind>();

    @JsonProperty("location")
    public GeoPoint Point;


    public ProcessedTrap(EnrichedTrap pdu) {
        this.enterprise = pdu.getEnterprise().toString();
        this.genericTrap = pdu.getGenericTrap();
        this.specificTrap = pdu.getSpecificTrap();
        this.timestamp = pdu.getTimestamp();
        this.agentAddress = pdu.getAgentAddress().toString();
        this.variableBindings = pdu.getVariableBindings();
        this.severity = pdu.getSeverity();
        this.Point = new GeoPoint(Math.random(),Math.random());
    }
}
