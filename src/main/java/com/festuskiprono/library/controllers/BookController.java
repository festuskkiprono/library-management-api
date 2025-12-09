package com.festuskiprono.library.controllers;

import com.festuskiprono.library.entities.Book;
import com.festuskiprono.library.repositories.BookRepository;
import com.festuskiprono.library.dtos.BorrowedBookDto;
import lombok.AllArgsConstructor;
import com.festuskiprono.library.mappers.BookMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {

   private final BookRepository bookRepository;
   private final BookMapper bookMapper;

    @GetMapping("/{title}")
    public List<BorrowedBookDto> getBook(
            @PathVariable(required = false) String title)
    {
       List<Book> books;
        if(title != null && !title.isBlank())
       {
           books = bookRepository.getBooksByTitleContainingIgnoreCase(title);
       }
       else
       {
           books = bookRepository.findAll();
       }
       return books.stream().map(bookMapper::toDto).toList();
    }
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
