package com.example.lms.controller;

import com.example.lms.security.JwtUtil;
import com.example.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    // USER REGISTRATION (API)
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        boolean admin = Boolean.parseBoolean(request.getOrDefault("admin", "false"));

        userService.registerUser(username, password, admin);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        return response;
    }

    // USER LOGIN (API) → RETURNS JWT TOKEN
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = jwtUtil.generateToken(username);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", username);
            response.put("type", "Bearer");

            return response;

        } catch (AuthenticationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid username or password"
            );
        }

    }
}
