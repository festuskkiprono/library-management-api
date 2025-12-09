package com.festuskiprono.library.repositories;

import com.festuskiprono.library.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Integer> {
    List<Book> getBooksByTitleContainingIgnoreCase(String title);
    List<Book> findByGenreId(Short genreId);
}
