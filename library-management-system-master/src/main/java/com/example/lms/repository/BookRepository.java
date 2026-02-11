package com.example.lms.repository;

import com.example.lms.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    // Inventory search only
    List<Book> findByAuthorNameContainingIgnoreCase(String authorName);

    List<Book> findByBookNameContainingIgnoreCase(String bookName);

    Page<Book> findByActiveTrueAndBookNameContainingIgnoreCase(
            String bookName,
            Pageable pageable
    );

    Page<Book> findByActiveTrueAndAuthorNameContainingIgnoreCase(
            String authorName,
            Pageable pageable
    );

    // USER SIDE
    List<Book> findByActiveTrue();

    Page<Book> findByActiveTrue(Pageable pageable);

    List<Book> findByActiveTrueAndBookNameContainingIgnoreCase(String bookName);
    List<Book> findByActiveTrueAndAuthorNameContainingIgnoreCase(String authorName);

    // ADMIN (optional)
    Page<Book> findByActiveFalse(Pageable pageable);

}
