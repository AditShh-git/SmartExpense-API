package com.Expense.Tracker.API.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBudgetRequest {

    @NotNull(message = "Budget cannot be null")
    @Positive(message = "Budget must be a positive number")
    private BigDecimal monthlyBudget;
}
