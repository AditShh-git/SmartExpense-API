package com.Expense.Tracker.API.controller;

import com.Expense.Tracker.API.dto.ExpenseCreationResponse;
import com.Expense.Tracker.API.dto.ExpenseRequest;
import com.Expense.Tracker.API.models.Expense;
import com.Expense.Tracker.API.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseCreationResponse> addExpense(@Valid @RequestBody ExpenseRequest expenseRequest){
        ExpenseCreationResponse response = expenseService.createExpense(expenseRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseRequest expenseRequest){
        Expense update = expenseService.updateExpense(id, expenseRequest);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses(@RequestParam(required = false) Long categoryId,
                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate from,
                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate to) {
        List<Expense> expenses = expenseService.getExpense(categoryId, from, to);
        return ResponseEntity.ok(expenses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id){
        expenseService.deleteExpense(id);
        return ResponseEntity.ok("Expense has been deleted");
    }
}
