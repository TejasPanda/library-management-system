package com.example.lms.controller;

import com.example.lms.model.Book;
import com.example.lms.model.BorrowRecord;
import com.example.lms.service.BookService;
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
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping("/admin/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.addBook(book));
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
    public List<Book> searchByAuthor(@RequestParam String authorName) {
        return bookService.searchByAuthor(authorName);
    }

    @GetMapping("/books/search/title")
    public List<Book> searchByTitle(@RequestParam String bookName) {
        return bookService.searchByTitle(bookName);
    }

    // =========================
    // USER BORROW DATA
    // =========================
    @GetMapping("/users/{username}/borrows")
    public List<BorrowRecord> getUserBorrowHistory(
            @PathVariable String username
    ) {
        return bookService.getBorrowHistory(username);
    }
}

