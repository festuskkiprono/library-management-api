package com.festuskiprono.library.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 255)
    @NotNull
    @Column(name = "author", nullable = false)
    private String author;

    @Size(max = 20)
    @Column(name = "isbn", length = 20)
    private String isbn;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @NotNull
    @Column(name = "total_copies", nullable = false)
    private Integer totalCopies;

    @NotNull
    @Column(name = "available_copies", nullable = false)
    private Integer availableCopies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;


    @OneToMany(mappedBy = "book")
    private Set<BorrowCartItem> borrowCartItems = new LinkedHashSet<>();

    @OneToMany(mappedBy = "book")
    private Set<BorrowedBook> borrowedBooks = new LinkedHashSet<>();

}