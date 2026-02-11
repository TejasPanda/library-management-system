package com.example.lms.controller;

import com.example.lms.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Helper: Add currentPath for navbar highlight
    private void addPath(Model model, HttpServletRequest request) {
        model.addAttribute("currentPath", request.getRequestURI());
    }

    // ================================
    // LOGIN PAGE
    // ================================
    @GetMapping("/login")
    public String loginPage(Model model, HttpServletRequest request) {

        addPath(model, request);

        // Prevent logged-in users from revisiting login page
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() &&
                !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/ui/books";
        }

        return "login";
    }

    // ================================
    // REGISTER PAGE
    // ================================
    @GetMapping("/register")
    public String registerPage(Model model, HttpServletRequest request) {
        addPath(model, request);
        return "register";
    }

    // REGISTER SUBMIT
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam(defaultValue = "false") boolean admin,
                               Model model,
                               HttpServletRequest request) {

        addPath(model, request);

        try {
            userService.registerUser(username, password, admin);
            return "redirect:/ui/login?registered";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
