package com.example.lms.controller;

import com.example.lms.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/ui")
public class UserHistoryController {

    private final BookService bookService;

    public UserHistoryController(BookService bookService) {
        this.bookService = bookService;
    }

    // Helper to inject current path for navbar active highlighting
    private void addPath(Model model, HttpServletRequest req) {
        model.addAttribute("currentPath", req.getRequestURI());
    }

    @GetMapping("/history")
    public String borrowHistory(
            Model model,
            Principal principal,
            HttpServletRequest request
    ) {
        addPath(model, request);

        String username = principal.getName();
        model.addAttribute("history",
                bookService.getBorrowHistory(username));


        return "borrow-history";  // HTML page name
    }
}
