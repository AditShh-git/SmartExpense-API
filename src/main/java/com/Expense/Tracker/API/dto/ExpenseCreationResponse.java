package com.Expense.Tracker.API.dto;

import com.Expense.Tracker.API.models.Expense;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseCreationResponse {

    private Expense expense;
    private String budgetWarning;

}
