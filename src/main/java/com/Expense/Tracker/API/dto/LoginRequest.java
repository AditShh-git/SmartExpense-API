package com.Expense.Tracker.API.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username or Email cannot be blank")
    private String loginIdentifier;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
