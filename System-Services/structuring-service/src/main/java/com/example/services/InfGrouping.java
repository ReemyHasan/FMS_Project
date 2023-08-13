package com.example.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Setter
@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
public class InfGrouping {
    @Autowired
    private KafkaService kafkaService;
    public static final List<String> toDeleteHeaders = Arrays.asList("Network_Interfaces_Discovery",
            "EtherLike-MIB_Discovery",
            "SNMP_traps_(fallback)",
            "Device_contact_details",
            "Interface",
            "Device_description",
            "Device_location",
            "Device_name",
            "System_object_ID"
    );
    public static final List<String> Behaviour = Arrays.asList("_Inbound_packets_discarded",
            "_Outbound_packets_discarded",
            "_Inbound_packets_with_errors",
            "_Outbound_packets_with_errors",
            "_Bits_sent",
            "_Bits_received"
    );

    public static final List<String> Type = Arrays.asList("_Speed","_Interface_type");
    public static final List<String> Status = Arrays.asList("_Operational_status");
    public static final List<String> Internal = Arrays.asList("0/1","0/2");
    public static final List<String> Peripheral = Arrays.asList("0/0");
    public static final List<String> External = Arrays.asList("0/3");

    public static List<String> newHeaders = new ArrayList<>();
    public static Boolean first = true;

    public void calc(JSONObject s, List<String> heads){
        LinkedHashMap<String,Object> newRecord = new LinkedHashMap<>();
        for (int i=0;i<heads.size();i++){
            String w = heads.get(i);
            Boolean sol = false;
            for (String h:toDeleteHeaders){
                if (!w.contains(h)) continue;
                sol = true;
                break;
            }
            if (sol) continue;
            if (first)
                newHeaders.add(w);
            try{
                Double y = Double.parseDouble(s.getString(w));
                newRecord.put(w,y);
            } catch (Exception e){
                newRecord.put(w,s.get(w));
            }
        }

        for (String prop : Behaviour) {
            Double sumInternal = 0.0,sumExternal = 0.0,sumPeripheral = 0.0;
            for (int i=0;i<heads.size();i++){
                String w = heads.get(i);
                if (!heads.get(i).contains(prop)) continue;
                for (String inf : Internal){
                    if (!heads.get(i).contains(inf)) continue;
                    sumInternal += Double.parseDouble(s.getString(w));
                }
                for (String inf : External){
                    if (!heads.get(i).contains(inf)) continue;
                    sumExternal += Double.parseDouble(s.getString(w));
                }
                for (String inf : Peripheral){
                    if (!heads.get(i).contains(inf)) continue;
                    sumPeripheral += Double.parseDouble(s.getString(w));
                }
            }
            if (first)
                newHeaders.add("EX"+prop);
            newRecord.put("EX"+prop,sumExternal);
            if (first)
                newHeaders.add("IN"+prop);
            newRecord.put("IN"+prop,sumInternal);
            if (first)
                newHeaders.add("P"+prop);
            newRecord.put("P"+prop,sumPeripheral);
        }
        for (String prop : Type) {
            Double sumInternal = 0.0,sumExternal = 0.0,sumPeripheral = 0.0;
            for (int i=0;i<heads.size();i++){
                String w = heads.get(i);
                if (!heads.get(i).contains(prop)) continue;
                for (String inf : Internal){
                    if (!heads.get(i).contains(inf)) continue;
                    sumInternal = Double.parseDouble(s.getString(w));
                }
                for (String inf : External){
                    if (!heads.get(i).contains(inf)) continue;
                    sumExternal = Double.parseDouble(s.getString(w));
                }
                for (String inf : Peripheral){
                    if (!heads.get(i).contains(inf)) continue;
                    sumPeripheral = Double.parseDouble(s.getString(w));
                }
            }
            if (first)
                newHeaders.add("EX"+prop);
            newRecord.put("EX"+prop,sumExternal);
            if (first)
                newHeaders.add("IN"+prop);
            newRecord.put("IN"+prop,sumInternal);
            if (first)
                newHeaders.add("P"+prop);
            newRecord.put("P"+prop,sumPeripheral);
        }
        for (String prop : Status) {
            Double sumInternal = 0.0,sumExternal = 0.0,sumPeripheral = 0.0;
            for (int i=0;i<heads.size();i++){
                String w = heads.get(i);
                if (!heads.get(i).contains(prop)) continue;
                for (String inf : Internal){
                    if (!heads.get(i).contains(inf)) continue;
                    sumInternal += Double.parseDouble(s.getString(w));
                }
                for (String inf : External){
                    if (!heads.get(i).contains(inf)) continue;
                    sumExternal += Double.parseDouble(s.getString(w));
                }
                for (String inf : Peripheral){
                    if (!heads.get(i).contains(inf)) continue;
                    sumPeripheral += Double.parseDouble(s.getString(w));
                }
            }
            if (first)
                newHeaders.add("EX"+prop);
            sumExternal /= (External.size());
            sumInternal /= (Internal.size());
            sumPeripheral /= (Peripheral.size());
            newRecord.put("EX"+prop,sumExternal);
            if (first)
                newHeaders.add("IN"+prop);
            newRecord.put("IN"+prop,sumInternal);
            if (first)
                newHeaders.add("P"+prop);
            newRecord.put("P"+prop,sumPeripheral);
        }
        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(newRecord);
        System.out.println(jsonString);
        kafkaService.send("Stream-Instance",jsonString);
    }
}
