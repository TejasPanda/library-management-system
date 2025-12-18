package com.example.lms.controller;

import com.example.lms.model.Book;
import com.example.lms.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/ui")
public class UIBookController {

    private final BookService bookService;

    public UIBookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Helper → Inject current path for navbar highlighting
    private void addPath(Model model, HttpServletRequest req) {
        model.addAttribute("currentPath", req.getRequestURI());
    }

    // ---------------------------------------------------------
    // USER: VIEW ALL BOOKS (PAGINATED + SORTABLE)
    // ---------------------------------------------------------
    @GetMapping("/books")
    public String showBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "bookName") String sort,
            Model model,
            HttpServletRequest request
    ) {
        addPath(model, request);

        Page<Book> bookPage = bookService.getBooksPage(page, size, sort);

        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("sort", sort);

        return "books";
    }

    // ---------------------------------------------------------
    // USER: BORROW BOOK
    // ---------------------------------------------------------
    @GetMapping("/books/borrow/{id}")
    public String borrowBook(@PathVariable int id) {
        bookService.borrowBook(id);
        return "redirect:/ui/books";
    }
    //user: return book
    @GetMapping("/books/return/{id}")
    public String returnBook(@PathVariable int id) {
        bookService.returnBook(id);
        return "redirect:/ui/books";
    }





    // ---------------------------------------------------------
    // USER PROFILE PAGE
    // ---------------------------------------------------------
    @GetMapping("/profile")
    public String userProfile(
            Model model,
            Principal principal,
            HttpServletRequest request
    ) {
        addPath(model, request);

        String username = principal.getName();
        model.addAttribute("username", username);
        model.addAttribute(
                "borrowedRecords",
                bookService.getActiveBorrowsForUser(username)
        );

        return "profile";
    }


    // ---------------------------------------------------------
    // SEARCH BY TITLE
    // ---------------------------------------------------------
    @GetMapping("/books/search/title")
    public String searchByTitle(
            @RequestParam String bookName,
            Model model,
            HttpServletRequest request
    ) {
        addPath(model, request);

        model.addAttribute("books", bookService.searchByTitle(bookName));
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);

        return "books";
    }

    // ---------------------------------------------------------
    // SEARCH BY AUTHOR
    // ---------------------------------------------------------
    @GetMapping("/books/search/author")
    public String searchByAuthor(
            @RequestParam String authorName,
            Model model,
            HttpServletRequest request
    ) {
        addPath(model, request);

        model.addAttribute("books", bookService.searchByAuthor(authorName));
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);

        return "books";
    }
}
