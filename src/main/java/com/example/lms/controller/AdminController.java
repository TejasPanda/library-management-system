package com.example.lms.controller;

import com.example.lms.model.Book;
import com.example.lms.model.BorrowRecord;
import com.example.lms.repository.BorrowRecordRepository;
import com.example.lms.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/ui/admin")
public class AdminController {

    private final BookService bookService;
    private final BorrowRecordRepository borrowRecordRepository;

    public AdminController(BookService bookService,
                           BorrowRecordRepository borrowRecordRepository) {
        this.bookService = bookService;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    // Helper for navbar
    private void addPath(Model model, HttpServletRequest request) {
        model.addAttribute("currentPath", request.getRequestURI());
    }

    // =============================
    // ADMIN DASHBOARD
    // =============================
    @GetMapping("")
    public String dashboard(Model model, HttpServletRequest request) {
        addPath(model, request);
        model.addAttribute("books", bookService.getAllBooks());
        return "admin";
    }

    // =============================
    // MANAGE BOOKS
    // =============================
    @GetMapping("/books")
    public String adminBooks(
            @RequestParam(defaultValue = "ALL") String filter,
            Model model,
            HttpServletRequest request
    ) {
        addPath(model, request);

        List<Book> books;

        switch (filter) {
            case "ACTIVE" -> books = bookService.getActiveBooks();
            case "ARCHIVED" -> books = bookService.getArchivedBooks();
            default -> books = bookService.getAllBooks();
        }

        model.addAttribute("books", books);
        model.addAttribute("filter", filter);

        return "admin";
    }



    @GetMapping("/books/add")
    public String addBookPage(Model model, HttpServletRequest request) {
        addPath(model, request);
        return "addBook";
    }

    @PostMapping("/books/add")
    public String addBook(@RequestParam String bookName,
                          @RequestParam String authorName) {

        Book b = new Book();
        b.setBookName(bookName);
        b.setAuthorName(authorName);
        bookService.addBook(b);

        return "redirect:/ui/admin/books";
    }



    // =============================
    // BORROW HISTORY + FILTERS
    // =============================
    @GetMapping("/borrows")
    public String borrowedBooks(
            @RequestParam(defaultValue = "ALL") String filter,
            Model model,
            HttpServletRequest request
    ) {
        addPath(model, request);

        List<BorrowRecord> records;

        switch (filter) {
            case "OVERDUE" ->
                    records = borrowRecordRepository
                            .findByReturnedAtIsNullAndDueDateBefore(
                                    LocalDate.now().atStartOfDay()
                            );

            case "BORROWED" ->
                    records = borrowRecordRepository.findByReturnedAtIsNull();

            case "RETURNED" ->
                    records = borrowRecordRepository.findByReturnedAtIsNotNull();

            default ->
                    records = borrowRecordRepository
                            .findAll(Sort.by(Sort.Direction.DESC, "borrowedAt"));
        }

        model.addAttribute("borrowedRecords", records);
        model.addAttribute("filter", filter);

        return "admin-borrowed";
    }
    @GetMapping("/books/archive/{id}")
    public String archiveBook(
            @PathVariable int id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            bookService.archiveBook(id);
            redirectAttributes.addFlashAttribute("success", "Book archived successfully");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/ui/admin/books";
    }

    @GetMapping("/books/restore/{id}")
    public String restoreBook(@PathVariable int id) {
        bookService.restoreBook(id);
        return "redirect:/ui/admin/books?filter=ARCHIVED";
    }



}
