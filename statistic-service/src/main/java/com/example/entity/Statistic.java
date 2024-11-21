package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistic {

    private String eventType;

    private Long userId;

    private String checkInDate;

    private String checkOutDate;

}
