package com.example.lms.service;

import com.example.lms.model.Role;
import com.example.lms.model.User;
import com.example.lms.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User registerUser(String username, String rawPassword, boolean admin){
        if(userRepository.existsByUsername(username)){
            throw new IllegalArgumentException("username already taken");
        }
        User user= new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoles(admin? Set.of(Role.ROLE_ADMIN,Role.ROLE_USER):Set.of(Role.ROLE_USER));
        return userRepository.save(user);
    }
    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }

    public String getLoggedInUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return null;  // no logged-in user
        }

        return auth.getName(); // username
    }

}
