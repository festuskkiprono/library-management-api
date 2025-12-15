package com.festuskiprono.library.controllers;

import com.festuskiprono.library.dtos.booksDtos.AvailableBookCountDto;
import com.festuskiprono.library.dtos.booksDtos.BookCreateDto;
import com.festuskiprono.library.entities.Book;
import com.festuskiprono.library.repositories.BookRepository;
import com.festuskiprono.library.dtos.booksDtos.BorrowedBookDto;
import com.festuskiprono.library.services.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import com.festuskiprono.library.mappers.BookMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {

   private final BookRepository bookRepository;
   private final BookMapper bookMapper;
   private final BookService bookService;

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

    @PostMapping

    public ResponseEntity<BorrowedBookDto> createBook(@Valid @RequestBody BookCreateDto bookCreateDto) {
        Book book = bookMapper.toEntity(bookCreateDto);
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookMapper.toDto(savedBook));
    }


    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // GET /books/genre/{id} - Get books by genre
    @GetMapping("/genre/{id}")
    public ResponseEntity<List<BorrowedBookDto>> getBooksByGenre(@PathVariable Short id) {
        List<Book> books = bookRepository.findByGenreId(id);
        if (books.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<BorrowedBookDto> bookDtos = books.stream()
                .map(bookMapper::toDto)
                .toList();
        return ResponseEntity.ok(bookDtos);
    }

    @PutMapping("/return/{id}")
    public ResponseEntity<AvailableBookCountDto> returnBook(
            @PathVariable int id) {

        Book updatedBook = bookService.returnBook(id);
        return ResponseEntity.ok(bookMapper.toAvailableBookCountDto(updatedBook));
    }

}
