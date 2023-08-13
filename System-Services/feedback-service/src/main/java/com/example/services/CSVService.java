package com.example.services;

import java.io.IOException;
import java.util.List;

import com.example.dto.RangeRequest;
import com.example.helper.CSVHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class CSVService {
    @Autowired
    ElasticService elasticService;
    public void process(MultipartFile file) {
        try {
            List<RangeRequest> rangeRequests = CSVHelper.csvToRangeRequest(file.getInputStream());
            for (RangeRequest rangeRequest:rangeRequests){
                elasticService.updateIndex(rangeRequest,"knowledge-base");
            }
            System.out.println(rangeRequests);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

}