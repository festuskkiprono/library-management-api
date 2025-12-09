package com.festuskiprono.library.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "borrow_rules")
public class BorrowRule {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "max_books", nullable = false)
    private Integer maxBooks;

    @NotNull
    @Column(name = "max_borrow_days", nullable = false)
    private Integer maxBorrowDays;

}