package com.example.web.controller;

import com.example.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/download")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<byte[]> downloadStatistics() {
        byte[] bytes = statisticService.getStatisticAsCsv();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=statistics.csv");
        headers.add("Content-Type", "text/csv; charset=UTF-8");

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
