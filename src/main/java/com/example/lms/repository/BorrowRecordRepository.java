package com.example.lms.repository;

import com.example.lms.model.BorrowRecord;
import com.example.lms.model.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    // User history
    List<BorrowRecord> findByUser_UsernameOrderByBorrowedAtDesc(String username);

    // Admin view
    List<BorrowRecord> findByStatus(BorrowStatus status);

    // Active borrow for return
    Optional<BorrowRecord>
    findTopByBook_BookIdAndUser_UsernameAndStatus(
            int bookId,
            String username,
            BorrowStatus status
    );

    List<BorrowRecord> findByUser_UsernameAndStatus(String username, BorrowStatus borrowStatus);
    List<BorrowRecord> findByReturnedAtIsNull();

    List<BorrowRecord> findByReturnedAtIsNotNull();

    List<BorrowRecord> findByReturnedAtIsNullAndDueDateBefore(LocalDateTime dueDate);

}
