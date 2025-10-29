package com.Expense.Tracker.API.service;

import com.Expense.Tracker.API.dto.CategoryRequest;
import com.Expense.Tracker.API.exception.ResourceNotFoundException;
import com.Expense.Tracker.API.models.Category;
import com.Expense.Tracker.API.models.User;
import com.Expense.Tracker.API.repository.CategoryRepository;
import com.Expense.Tracker.API.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Username not found"));
    }

    public String createCategory(CategoryRequest categoryRequest) {

        User user = getCurrentUser();
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setUser(user);
        categoryRepository.save(category);

        return "Category created";
    }

    public String updateCategory(Long id, CategoryRequest categoryRequest) {

        User user = getCurrentUser();
        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setName(categoryRequest.getName());
        categoryRepository.save(category);

        return "Category updated";
    }

    public List<Category> getAllCategories() {
        User user = getCurrentUser();
        return categoryRepository.findByUser(user);
    }


    public  String deleteCategory(Long id) {
        User user = getCurrentUser();
        Category category = categoryRepository.findByIdAndUser(id,user)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        categoryRepository.delete(category);
        return "Category deleted";
    }

}
