package com.Expense.Tracker.API.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySummaryResponse {
    private String month;
    private BigDecimal totalExpense;
    private String topCategory;
    private Map<String, BigDecimal> categoryBreakdown;
}
