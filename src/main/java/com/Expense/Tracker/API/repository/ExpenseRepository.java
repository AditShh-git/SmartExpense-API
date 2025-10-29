package com.Expense.Tracker.API.repository;

import com.Expense.Tracker.API.dto.CategorySummaryDTO;
import com.Expense.Tracker.API.models.Category;
import com.Expense.Tracker.API.models.Expense;
import com.Expense.Tracker.API.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);
    Optional<Expense> findByIdAndUser(Long id,User user);
    List<Expense> findByUserAndCategory(User user, Category category);
    List<Expense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);


    @Query("SELECT new com.Expense.Tracker.API.dto.CategorySummaryDTO(c.name, SUM(e.amount)) " +
            "FROM Expense e JOIN e.category c " +
            "WHERE e.user = :user " +
            "AND FUNCTION('MONTH', e.date) = :month " +
            "AND FUNCTION('YEAR', e.date) = :year " +
            "GROUP BY c.name")
    List<CategorySummaryDTO> getCategorySummaries(
            @Param("user") User user,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("SELECT SUM(e.amount) FROM Expense e " +
            "WHERE e.user = :user " +
            "AND FUNCTION('MONTH', e.date) = :month " +
            "AND FUNCTION('YEAR', e.date) = :year")
    BigDecimal getTotalExpensesForMonth(
            @Param("user") User user,
            @Param("month") int month,
            @Param("year") int year
    );

}
