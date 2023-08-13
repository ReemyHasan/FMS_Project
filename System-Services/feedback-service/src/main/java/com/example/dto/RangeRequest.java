package com.example.dto;

import co.elastic.clients.util.DateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class RangeRequest {
    long start;
    long end;
    String clas;
}
