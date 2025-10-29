package com.Expense.Tracker.API.controller;

import com.Expense.Tracker.API.dto.UserBudgetRequest;
import com.Expense.Tracker.API.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

private final UserService userService;

    @PutMapping("/budget")
    public ResponseEntity<Map<String,String>> updateBudget(@Valid @RequestBody UserBudgetRequest  userBudgetRequest){
        userService.setBudget(userBudgetRequest);

        return ResponseEntity.ok(Map.of("message", "Budget updated successfully"));
    }

    @GetMapping("/budget")
    public ResponseEntity<Map<String, BigDecimal>> getBudget() {
        BigDecimal budget = userService.getBudget();

        Map<String, BigDecimal> response = new HashMap<>();
        response.put("monthlyBudget", budget);

        return ResponseEntity.ok(response);
    }
}
