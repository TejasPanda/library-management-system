package com.example.lms.repository;

import com.example.lms.model.BorrowRecord;
import com.example.lms.model.BorrowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    // User history
    Page<BorrowRecord> findByUser_UsernameOrderByBorrowedAtDesc(
            String username,
            Pageable pageable
    );

    // Admin view
    List<BorrowRecord> findByStatus(BorrowStatus status);

    // Active borrow for return
    Optional<BorrowRecord>
    findTopByBook_BookIdAndUser_UsernameAndStatus(
            int bookId,
            String username,
            BorrowStatus status
    );
    Page<BorrowRecord> findByUser_UsernameAndReturnedAtIsNull(
            String username,
            Pageable pageable
    );





    Page<BorrowRecord> findByReturnedAtIsNull(Pageable pageable);

    Page<BorrowRecord> findByReturnedAtIsNotNull(Pageable pageable);

    Page<BorrowRecord> findByReturnedAtIsNullAndDueDateBefore(
            LocalDateTime date,
            Pageable pageable
    );

}
