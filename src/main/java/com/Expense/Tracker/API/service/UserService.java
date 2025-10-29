package com.Expense.Tracker.API.service;

import com.Expense.Tracker.API.dto.UserBudgetRequest;
import com.Expense.Tracker.API.exception.ResourceNotFoundException;
import com.Expense.Tracker.API.models.User;
import com.Expense.Tracker.API.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Username not found"));
    }

    @Transactional
    public void setBudget(UserBudgetRequest monthlyBudget) {
        User user = getCurrentUser();

        user.setMonthlyBudget(monthlyBudget.getMonthlyBudget());
        userRepository.save(user);
    }

    public BigDecimal getBudget() {
        User user = getCurrentUser();
        return user.getMonthlyBudget();
    }
}
