package com.Expense.Tracker.API.service;

import com.Expense.Tracker.API.dto.ExpenseCreationResponse;
import com.Expense.Tracker.API.dto.ExpenseRequest;
import com.Expense.Tracker.API.exception.ResourceNotFoundException;
import com.Expense.Tracker.API.models.Category;
import com.Expense.Tracker.API.models.Expense;
import com.Expense.Tracker.API.models.User;
import com.Expense.Tracker.API.repository.CategoryRepository;
import com.Expense.Tracker.API.repository.ExpenseRepository;
import com.Expense.Tracker.API.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Username not found"));
    }

//    public Expense createExpense(ExpenseRequest  expenseRequest) {
//
//        User user = getCurrentUser();
//        Category category = categoryRepository.findByIdAndUser(expenseRequest.getCategoryId(),user)
//                .orElseThrow(() -> new RuntimeException("Category not found or does not belong to user"));
//
//        Expense expense = new Expense();
//        expense.setCategory(category);
//        expense.setUser(user);
//        expense.setDate(LocalDate.now());
//        expense.setAmount(expenseRequest.getAmount());
//        expense.setDescription(expenseRequest.getDescription());
//        expenseRepository.save(expense);
//        return expense;
//
//    }

    public Expense updateExpense(Long id, ExpenseRequest expenseRequest) {
        User user = getCurrentUser();

        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found or does not belong to user"));

        Category category = categoryRepository.findByIdAndUser(expenseRequest.getCategoryId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found or does not belong to user"));

        expense.setAmount(expenseRequest.getAmount());
        expense.setDescription(expenseRequest.getDescription());
        expense.setDate(expenseRequest.getDate());
        expense.setCategory(category);

        return expenseRepository.save(expense);

    }

    public void deleteExpense(Long id) {
        User user = getCurrentUser();
        Expense expense = expenseRepository.findByIdAndUser(id,user)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        expenseRepository.delete(expense);
    }

    public List<Expense> getExpense(Long categoryId, LocalDate from, LocalDate to) {
        User user = getCurrentUser();

        if (categoryId != null) {
            Category category = categoryRepository.findByIdAndUser(categoryId, user)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

            return expenseRepository.findByUserAndCategory(user,category);
        } else if (from != null && to!= null) {
            return expenseRepository.findByUserAndDateBetween(user,from,to);
        }else  {
            return expenseRepository.findByUser(user);
        }
    }

    @Transactional
    public ExpenseCreationResponse createExpense(ExpenseRequest expenseRequest) {
        User user = getCurrentUser();
        Category category = categoryRepository.findByIdAndUser(expenseRequest.getCategoryId(), user)
                .orElseThrow(() -> new RuntimeException("Category not found or does not belong to user"));


        Expense expense = new Expense();
        expense.setCategory(category);
        expense.setUser(user);
        expense.setAmount(expenseRequest.getAmount());
        expense.setDescription(expenseRequest.getDescription());
        expense.setDate(expenseRequest.getDate());

        Expense savedExpense = expenseRepository.save(expense);

        String budgetWarning = checkBudget(user, savedExpense.getDate());

        return new ExpenseCreationResponse(savedExpense, budgetWarning);
    }

    private String checkBudget(User user, LocalDate expenseDate) {

        BigDecimal budget = user.getMonthlyBudget();


        if (budget == null || budget.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        int month = expenseDate.getMonthValue();
        int year = expenseDate.getYear();

        BigDecimal totalExpenses = expenseRepository.getTotalExpensesForMonth(user, month, year);

        if (totalExpenses == null) {
            totalExpenses = BigDecimal.ZERO;
        }

        if (totalExpenses.compareTo(budget) > 0) {
            BigDecimal overAmount = totalExpenses.subtract(budget);
            return String.format("Warning: You are now $%s over your monthly budget of $%s!",
                    overAmount.toString(), budget.toString());
        } else if (totalExpenses.compareTo(budget) == 0) {
            return "Warning: You have reached your monthly budget limit!";
        }


        return null;
    }
}
