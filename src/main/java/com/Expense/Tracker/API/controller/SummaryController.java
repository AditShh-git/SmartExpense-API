package com.Expense.Tracker.API.controller;

import com.Expense.Tracker.API.dto.MonthlySummaryResponse;
import com.Expense.Tracker.API.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/summary")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping("/monthly")
    public ResponseEntity<MonthlySummaryResponse> getMonthlySummary(@RequestParam int month, @RequestParam int year){

        MonthlySummaryResponse summary = summaryService.getMonthlySummary(month, year);
        return ResponseEntity.ok(summary);
    }
}
