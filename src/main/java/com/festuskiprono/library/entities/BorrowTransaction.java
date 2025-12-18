package com.festuskiprono.library.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "borrow_transactions")
public class BorrowTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "borrow_tx_seq")
    @SequenceGenerator(name = "borrow_tx_seq", sequenceName = "borrow_transactions_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Size(max = 30)
    @NotNull
    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @NotNull
    @Column(name = "borrowed_date", nullable = false)
    private LocalDate borrowedDate;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "returned_date")
    private LocalDate returnedDate;


    @OneToMany(mappedBy = "borrowTransaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BorrowedBook> borrowedBooks = new ArrayList<>();


    public LocalDate getBorrowDate() {
        return this.borrowedDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowedDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return this.returnedDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnedDate = returnDate;
    }
}