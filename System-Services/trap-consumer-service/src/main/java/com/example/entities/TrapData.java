package com.example.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.snmp4j.PDUv1;
import org.snmp4j.smi.VariableBinding;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    public TrapData(PDUv1 pdu) {
        this.enterprise = pdu.getEnterprise().toString();
        this.genericTrap = pdu.getGenericTrap();
        this.specificTrap = pdu.getSpecificTrap();
        this.timestamp = pdu.getTimestamp();
        this.agentAddress = pdu.getAgentAddress().toString();
        List<VariableBinding> trapVariableBindings = pdu.getAll();

        for (VariableBinding vb : trapVariableBindings) {
            this.variableBindings.add(new VarBind(vb));
        }
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
