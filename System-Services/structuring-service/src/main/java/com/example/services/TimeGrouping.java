package com.example.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Setter
@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
public class TimeGrouping {

    @Autowired
    private InfGrouping infGrouping = new InfGrouping();
    Integer T = 180;
    List<String> headers = new ArrayList<>();
    List<JSONObject> records = new ArrayList<JSONObject>();
    List<Integer> numOfParameters = new ArrayList<Integer>();
    Integer total = 0;
    public TimeGrouping(List<String> csvList){
        T = 180;
        headers = csvList;
        records = new ArrayList<JSONObject>();
        numOfParameters = new ArrayList<Integer>();
        total = 0;
    }
    public void addRecord(JSONObject rec){
        headers.clear();
        for (String s:rec.keySet()){
            headers.add(s);
        }
        int temp = 0;
        for (String s : headers) {
            String ret = rec.getString(s);
            if (ret.contains("-1")) continue;
            if (ret.contains("?")) continue;
            if (ret.trim().isEmpty()) continue;
            temp++;
        }
        if (records.size() == 0) {
            numOfParameters.add(temp);
            records.add(rec);
            total += temp;
            return;
        }
        Integer now = Integer.parseInt(rec.getString("timestamp"));
        Integer beginning = Integer.parseInt(records.get(0).getString("timestamp"));
        Integer diff = (now-beginning);
        if (diff < T){
            records.add(rec);
            numOfParameters.add(temp);
        }
        else{
            this.cleanRecords();
            records.add(rec);
            total += temp;
            numOfParameters.add(temp);
            return;
        }
    }
    public void cleanRecords(){
        System.out.println("Hey");
        int ind = -1;
        Double sum = 0.0;
        Double Q3 = 0.0;
        Double tot = Double.parseDouble(total.toString());
        Q3 = (3.0/4.0)*tot;
//        if (total%2 == 1) Q3 = 3.0*(tot+1.0)/4.0;
//        else Q3 = 3.0*tot/4.0;
        LinkedHashMap<String,Object> cur = new LinkedHashMap<>();
        for(int i=0;i<numOfParameters.size();i++){
            Integer x = numOfParameters.get(i);
            sum += x;
            if (sum >= Q3){
                ind = i;
                break;
            }
        }
        Object u = records.get(ind).get("timestamp");
        cur.put("timestamp",u);
        Integer last = Integer.parseInt(records.get(records.size()-1).getString("timestamp"));
        Integer beginning = Integer.parseInt(records.get(0).getString("timestamp"));
        Integer range = last - beginning;
        for (int i=0;i<headers.size();i++){
            if (headers.get(i) == "timestamp") continue;
            int mx = -1;
            Integer mxVal = -5;
            Double mxVal2 = -5.0;
            for (int j=0;j<records.size();j++){
                try{
                    Double y = Double.parseDouble(records.get(j).getString(headers.get(i)));
                    //System.out.println("Double");
                    if (y >= mxVal2){
                        mx = j;
                        mxVal2 = y;
                    }
                }
                catch(Exception e2){
                    //System.out.println("String");
                    mx = j;
                }
            }
            cur.put(headers.get(i),records.get(mx).get(headers.get(i)));
        }
        cur.put("range",range);
        List<String> wow = new ArrayList<String>();
        wow.addAll(headers);
        wow.add("range");
        JSONObject cur2 = new JSONObject(cur);
        infGrouping.calc(cur2,wow);
        records.clear();
        total = 0;
        numOfParameters.clear();
    }
}
