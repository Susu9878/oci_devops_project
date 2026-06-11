package com.springboot.MyTodoList.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.MyTodoList.DTO.LoginRequestDTO;
import com.springboot.MyTodoList.DTO.RegisterRequestDTO;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.repository.UserRepository;
import com.springboot.MyTodoList.security.JwtUtil;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String register(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase();
        user.setEmail(normalizedEmail);

        user.setUsername(request.getUsername());
        user.setPhoneNumber(request.getPhoneNumber());

        // HASH PASSWORD
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setIsManager(false);
        user.setTeamId(null);

        userRepository.save(user);

        return jwtUtil.generateToken(user.getEmail());
    }

    public String login(LoginRequestDTO request) {
        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase();

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        boolean matches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword());

        if (!matches) {
            throw new RuntimeException("Invalid username or password");
        }

        return jwtUtil.generateToken(user.getEmail());
    }

}
