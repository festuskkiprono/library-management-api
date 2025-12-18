package com.festuskiprono.library.dtos;

import com.festuskiprono.library.dtos.booksDtos.BorrowedBookDto;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class BorrowTransactionDto {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status; // ACTIVE, RETURNED, OVERDUE
    private List<BorrowedBookDto> books;
    private Integer totalBooks;
}