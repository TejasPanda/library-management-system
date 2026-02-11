package com.example.lms.controller;

import com.example.lms.model.Book;
import com.example.lms.model.BorrowRecord;
import com.example.lms.repository.BorrowRecordRepository;
import com.example.lms.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public String dashboard(
            @RequestParam(defaultValue = "0") int page,
            Model model,
            HttpServletRequest request
    ) {
        addPath(model, request);

        Page<Book> bookPage = bookService.getAllBooks(page, 10);

        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", bookPage.getNumber());
        model.addAttribute("totalPages", bookPage.getTotalPages());

        return "admin";
    }


    // =============================
    // MANAGE BOOKS
    // =============================
    @GetMapping("/books")
    public String adminBooks(
            @RequestParam(defaultValue = "ALL") String filter,
            @RequestParam(defaultValue = "0") int page,
            Model model,
            HttpServletRequest request
    ) {
        addPath(model, request);

        Page<Book> bookPage;

        switch (filter) {
            case "ACTIVE" ->
                    bookPage = bookService.getActiveBooks(page, 10);
            case "ARCHIVED" ->
                    bookPage = bookService.getArchivedBooks(page, 10);
            default ->
                    bookPage = bookService.getAllBooks(page, 10);
        }

        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", bookPage.getNumber());
        model.addAttribute("totalPages", bookPage.getTotalPages());
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
            @RequestParam(defaultValue = "0") int page,
            Model model,
            HttpServletRequest request
    ) {
        addPath(model, request);

        Pageable pageable = PageRequest.of(
                page,
                10,
                Sort.by(Sort.Direction.DESC, "borrowedAt")
        );

        Page<BorrowRecord> recordPage;

        switch (filter) {
            case "OVERDUE" ->
                    recordPage = borrowRecordRepository
                            .findByReturnedAtIsNullAndDueDateBefore(
                                    LocalDate.now().atStartOfDay(),
                                    pageable
                            );

            case "BORROWED" ->
                    recordPage = borrowRecordRepository
                            .findByReturnedAtIsNull(pageable);

            case "RETURNED" ->
                    recordPage = borrowRecordRepository
                            .findByReturnedAtIsNotNull(pageable);

            default ->
                    recordPage = borrowRecordRepository
                            .findAll(pageable);
        }

        model.addAttribute("borrowedRecords", recordPage.getContent());
        model.addAttribute("currentPage", recordPage.getNumber());
        model.addAttribute("totalPages", recordPage.getTotalPages());
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
