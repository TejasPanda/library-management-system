package com.example.lms.service;

import com.example.lms.model.Book;
import com.example.lms.model.BorrowRecord;
import com.example.lms.model.BorrowStatus;
import com.example.lms.model.User;
import com.example.lms.repository.BookRepository;
import com.example.lms.repository.BorrowRecordRepository;
import com.example.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository repo;

    @Autowired
    private UserService userService;
    @Autowired
    private BorrowRecordRepository borrowRecordRepo;
    @Autowired
    private UserRepository userRepository;

    public List<Book> getAllBooks() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "bookId"));
    }


    public Book addBook(Book book) {
        return repo.save(book);
    }


    public Book getBookById(int bookId) {
        return repo.findById(bookId).orElse(null);
    }

    @Transactional
    public void borrowBook(int bookId) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Book book = repo.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (!book.isActive()) {
            throw new IllegalStateException("Book is archived");
        }

        if (book.isIssued()) {
            throw new IllegalStateException("Book already borrowed");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        book.setIssued(true);

        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setUser(user);
        record.setBorrowedAt(LocalDateTime.now());
        record.setDueDate(LocalDateTime.now().plusDays(14));
        record.setStatus(BorrowStatus.BORROWED);

        borrowRecordRepo.save(record);
    }




    // Only the borrower can return
    @Transactional
    public String returnBook(int bookId) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        BorrowRecord record = borrowRecordRepo
                .findTopByBook_BookIdAndUser_UsernameAndStatus(
                        bookId,
                        username,
                        BorrowStatus.BORROWED
                )
                .orElseThrow(() ->
                        new IllegalStateException("You did not borrow this book"));

        record.setReturnedAt(LocalDateTime.now());
        record.setStatus(BorrowStatus.RETURNED);

        Book book = record.getBook();
        book.setIssued(false);

        borrowRecordRepo.save(record);
        repo.save(book);

        return "Book returned";
    }


    public boolean checkAvailability(int bookId) {
        Optional<Book> bookOpt = repo.findById(bookId);
        return bookOpt
                .map(book -> book.isActive() && !book.isIssued())
                .orElse(false);

    }

    public List<Book> searchByTitle(String bookName) {
        return repo.findByActiveTrueAndBookNameContainingIgnoreCase(bookName);
    }

    public List<Book> searchByAuthor(String authorName) {
        return repo.findByActiveTrueAndAuthorNameContainingIgnoreCase(authorName);
    }


    // Get all borrowed books of a user

    public List<BorrowRecord> getBorrowHistory(String username) {
        return borrowRecordRepo
                .findByUser_UsernameOrderByBorrowedAtDesc(username);
    }

    public List<BorrowRecord> getAllActiveBorrows() {
        return borrowRecordRepo.findByStatus(BorrowStatus.BORROWED);
    }



    public Page<Book> getBooksPage(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return repo.findByActiveTrue(pageable);
    }

    public List<BorrowRecord> getActiveBorrowsForUser(String username) {
        return borrowRecordRepo.findByUser_UsernameAndStatus(
                username, BorrowStatus.BORROWED);
    }
    // -------------------------
    // SOFT DELETE (ARCHIVE)
    // -------------------------
    @Transactional
    public void archiveBook(int bookId) {

        Book book = repo.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        // 🚫 BUSINESS RULE
        if (book.isIssued()) {
            throw new IllegalStateException("Cannot archive a borrowed book");
        }

        book.setActive(false);
        repo.save(book);
    }


    // -------------------------
    // RESTORE BOOK
    // -------------------------
    @Transactional
    public void restoreBook(int bookId) {
        Book book = repo.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setActive(true);
        repo.save(book);

    }

    // -------------------------
    // FILTERS
    // -------------------------
    public List<Book> getActiveBooks() {
        return repo.findByActiveTrue();
    }

    public List<Book> getArchivedBooks() {
        return repo.findByActiveFalse();
    }

}
