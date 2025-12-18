package com.festuskiprono.library.repositories;

import com.festuskiprono.library.entities.BorrowTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowTransactionRepository extends JpaRepository<BorrowTransaction, Long> {

    // Find all transactions for a specific user
    List<BorrowTransaction> findByUserId(Long userId);

    // Find active (not returned) transactions for a user
    List<BorrowTransaction> findByUserIdAndReturnedDateIsNull(Long userId);

    // Find overdue transactions
    @Query("SELECT bt FROM BorrowTransaction bt WHERE bt.dueDate < :currentDate AND bt.returnedDate IS NULL")
    List<BorrowTransaction> findOverdueTransactions(LocalDate currentDate);

    // Find all active transactions
    List<BorrowTransaction> findByReturnedDateIsNull();

    List<BorrowTransaction> findByUserIdAndReturnDateIsNull(Long userId);
}