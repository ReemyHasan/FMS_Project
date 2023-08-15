package com.example.controllers;

import com.example.dto.RangeRequest;
import com.example.services.ElasticService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback/knowledge")
@RequiredArgsConstructor
@CrossOrigin("*")
public class HandleKnowledge {
    @Autowired
    ElasticService elasticService;
    @PostMapping("/range")
    public void rangeQuery(@RequestBody RangeRequest rangeRequest){
        System.out.println(rangeRequest.getStart());
        System.out.println(rangeRequest.getEnd());
        elasticService.updateIndex(rangeRequest,"knowledge-base");
    }

    @GetMapping("/reset")
    public void resetKnowledge(){
        elasticService.deleteAll();
    }

    @GetMapping("/initial")
    public String addInitial(){
        String s = elasticService.addInitialData();
        return s;
    }


}
