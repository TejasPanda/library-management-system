package com.example.lms.controller;

import com.example.lms.model.BorrowRecord;
import com.example.lms.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String userHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Principal principal,
            Model model
    ) {

        Page<BorrowRecord> recordPage =
                bookService.getBorrowHistory(
                        principal.getName(),
                        page,
                        size
                );

        model.addAttribute("history", recordPage.getContent());
        model.addAttribute("currentPage", recordPage.getNumber());
        model.addAttribute("totalPages", recordPage.getTotalPages());

        return "borrow-history";
    }

}
