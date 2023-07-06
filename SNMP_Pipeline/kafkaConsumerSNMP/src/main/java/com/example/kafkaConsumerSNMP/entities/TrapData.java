package com.example.kafkaConsumerSNMP.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.snmp4j.PDUv1;
import org.snmp4j.smi.VariableBinding;

import java.util.ArrayList;
import java.util.List;

public class TrapData {
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
    @JsonProperty("variableBindings")
    public List<VarBind> variableBindings = new ArrayList<VarBind>();
    public TrapData(String enterprise, String agentAddress, int genericTrap, int specificTrap, long timestamp, List<VariableBinding> trapVariableBindings) {
        this.enterprise = enterprise;
        this.genericTrap = genericTrap;
        this.specificTrap = specificTrap;
        this.timestamp = timestamp;
        this.agentAddress = agentAddress;
        for (VariableBinding vb : trapVariableBindings) {
            this.variableBindings.add(new VarBind(vb));
        }
    }
    public TrapData(PDUv1 pdu){
        this.enterprise = pdu.getEnterprise().toString();
        this.genericTrap = pdu.getGenericTrap();
        this.specificTrap = pdu.getSpecificTrap();
        this.timestamp = pdu.getTimestamp();
        this.agentAddress = pdu.getAgentAddress().toString();
        List<VariableBinding> trapVariableBindings = pdu.getAll();

        for (VariableBinding vb : trapVariableBindings){
            this.variableBindings.add(new VarBind(vb));
        }
    }

    public TrapData() {
    }

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    public int getGenericTrap() {
        return genericTrap;
    }

    public void setGenericTrap(int genericTrap) {
        this.genericTrap = genericTrap;
    }

    public int getSpecificTrap() {
        return specificTrap;
    }

    public void setSpecificTrap(int specificTrap) {
        this.specificTrap = specificTrap;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<VarBind> getVariableBindings() {
        return variableBindings;
    }

    public void setVariableBindings(List<VarBind> variableBindings) {
        this.variableBindings = variableBindings;
    }

    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }
    public void print(){
        System.out.println("enterprise = " + this.getEnterprise());
        System.out.println("enterprise = " + this.getAgentAddress());
        System.out.println("genericTrap = " + this.getGenericTrap());
        System.out.println("specificTrap = " + this.getSpecificTrap());
        System.out.println("timestamp = " + this.getTimestamp());
        System.out.println("variableBindings = {");
        for (VarBind v : this.getVariableBindings()){
            v.print();
        }
        System.out.println("}");
    }
}
