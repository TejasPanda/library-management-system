package com.example.lms.controller;

import com.example.lms.model.Book;
import com.example.lms.model.BorrowRecord;
import com.example.lms.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class ApiBookController {

    private final BookService bookService;

    public ApiBookController(BookService bookService) {
        this.bookService = bookService;
    }

    // =========================
    // BOOK INVENTORY
    // =========================
    @GetMapping("/books")
    public Page<Book> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return bookService.getAllBooks(page, size);
    }





    // =========================
    // BORROW / RETURN
    // =========================
    @PutMapping("/books/{bookId}/borrow")
    public ResponseEntity<Void> borrowBook(@PathVariable int bookId) {
        bookService.borrowBook(bookId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/books/{bookId}/return")
    public ResponseEntity<Void> returnBook(@PathVariable int bookId) {
        bookService.returnBook(bookId);
        return ResponseEntity.ok().build();
    }




    // =========================
    // SEARCH
    // =========================
    @GetMapping("/books/search/author")
    public Page<Book> searchByAuthor(
            @RequestParam String authorName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return bookService.searchByAuthor(authorName, page, size);
    }


    @GetMapping("/books/search/title")
    public Page<Book> searchByTitle(
            @RequestParam String bookName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return bookService.searchByTitle(bookName, page, size);
    }


    // =========================
    // USER BORROW DATA
    // =========================
    @GetMapping("/users/{username}/borrows")
    public Page<BorrowRecord> getUserBorrowHistory(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return bookService.getBorrowHistory(username, page, size);
    }

}

