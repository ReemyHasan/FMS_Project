package com.example.SnmpReciever.enitites;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.snmp4j.smi.VariableBinding;

public class VarBind {
    @JsonProperty("oid")
    public String oid;
    @JsonProperty("value")
    public String value;

    public VarBind(String oid, String value) {
        this.oid = oid;
        this.value = value;
    }
    public VarBind(VariableBinding vb){
        this.oid = vb.getOid().toString();
        this.value = vb.getVariable().toString();
    }

    public VarBind() {
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public void print(){
        System.out.println("oid = "+ this.getOid() + "," + "value = "+ this.getValue());
    }
}
