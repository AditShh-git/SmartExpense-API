package com.Expense.Tracker.API.service;

import com.Expense.Tracker.API.dto.AuthResponse;
import com.Expense.Tracker.API.dto.LoginRequest;
import com.Expense.Tracker.API.dto.RegisterRequest;
import com.Expense.Tracker.API.exception.BadRequestException;
import com.Expense.Tracker.API.models.Roles;
import com.Expense.Tracker.API.models.User;
import com.Expense.Tracker.API.repository.UserRepository;
import com.Expense.Tracker.API.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public String registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BadRequestException("ERROR : Username is already in use");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("ERROR : Email is already in use");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(Set.of(Roles.USER));

        userRepository.save(user);

        return "User registered successfully";
    }

    public AuthResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLoginIdentifier(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String username = authentication.getName();
        String token = jwtTokenProvider.generateJwtToken(username);

        return new  AuthResponse("Bearer", token);
    }
}
