package com.example.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.example.dto.RangeRequest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;


public class CSVHelper {
    public static String TYPE = "text/csv";
    static String[] HEADERs = { "time1", "time2", "fault"};

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<RangeRequest> csvToRangeRequest(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<RangeRequest> rangeRequests = new ArrayList<RangeRequest>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                RangeRequest rangeRequest = new RangeRequest(
                        Long.parseLong(csvRecord.get("time1")),
                        Long.parseLong(csvRecord.get("time2")),
                        csvRecord.get("fault")
                );

                rangeRequests.add(rangeRequest);
            }
            return rangeRequests;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

}