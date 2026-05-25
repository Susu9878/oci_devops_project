package com.springboot.MyTodoList.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.MyTodoList.DTO.AuthResponseDTO;
import com.springboot.MyTodoList.DTO.LoginRequestDTO;
import com.springboot.MyTodoList.DTO.RegisterRequestDTO;
import com.springboot.MyTodoList.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponseDTO register(
            @Valid @RequestBody RegisterRequestDTO request) {

        String token = authService.register(request);

        return new AuthResponseDTO(token, "User registered successfully");
    }

    @PostMapping("/login")
    public AuthResponseDTO login(
            @Valid @RequestBody LoginRequestDTO request) {

        String token = authService.login(request);

        return new AuthResponseDTO(token, "Login successful");
    }
}
