package com.example.kafkaConsumerSNMP.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.snmp4j.smi.VariableBinding;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class VarBind {
    @JsonProperty("oid")
    public String oid;
    @JsonProperty("value")
    public String value;

    public VarBind(VariableBinding vb){
        this.oid = vb.getOid().toString();
        this.value = vb.getVariable().toString();
    }

    public void print(){
        System.out.println("oid = "+ this.getOid() + "," + "value = "+ this.getValue());
    }
}
