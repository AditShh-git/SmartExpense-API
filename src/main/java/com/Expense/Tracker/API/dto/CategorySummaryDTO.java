package com.Expense.Tracker.API.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategorySummaryDTO {
    private String categoryName;
    private BigDecimal totalAmount;
}
