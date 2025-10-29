package com.Expense.Tracker.API.service;

import com.Expense.Tracker.API.dto.CategorySummaryDTO;
import com.Expense.Tracker.API.dto.MonthlySummaryResponse;
import com.Expense.Tracker.API.models.User;
import com.Expense.Tracker.API.repository.CategoryRepository;
import com.Expense.Tracker.API.repository.ExpenseRepository;
import com.Expense.Tracker.API.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    private User getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));
    }

    public MonthlySummaryResponse getMonthlySummary(int month, int year){
        User user = getCurrentUser();

        List<CategorySummaryDTO> categorySummaries = expenseRepository.getCategorySummaries(user, month, year);

        BigDecimal totalExpense = BigDecimal.ZERO;
        String topCategory = "N/A";
        BigDecimal maxCategoryAmount = BigDecimal.ZERO;
        Map<String, BigDecimal> categoryBreakdown = new HashMap<>();

        for (CategorySummaryDTO categorySummaryDTO : categorySummaries) {
            String categoryName = categorySummaryDTO.getCategoryName();
            BigDecimal totalAmount = categorySummaryDTO.getTotalAmount();

            totalExpense = totalExpense.add(totalAmount);

            categoryBreakdown.put(categoryName, totalAmount);

            if (totalAmount.compareTo(maxCategoryAmount) > 0) {
                maxCategoryAmount = totalAmount;
                topCategory = categoryName;
            }
        }
        String monthName = Month.of(month).name();
        monthName = monthName.substring(0,1) +  monthName.substring(1).toLowerCase();
        String formattedMonth = monthName + " " + year;

        return new MonthlySummaryResponse(
                formattedMonth,
                totalExpense,
                topCategory,
                categoryBreakdown
        );
    }
}
