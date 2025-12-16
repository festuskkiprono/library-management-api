package com.festuskiprono.library.services;

import com.festuskiprono.library.entities.Book;
import com.festuskiprono.library.entities.CartItem;
import com.festuskiprono.library.mappers.BookMapper;
import com.festuskiprono.library.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;



    public Book returnBook(int bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));



        book.setAvailableCopies(book.getAvailableCopies() + 1);

        return bookRepository.save(book);
    }
    public void returnBooksFromCart(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            Book book = item.getBook();
            int copiesToReturn = item.getCopies();

            book.setAvailableCopies(book.getAvailableCopies() + copiesToReturn);
            bookRepository.save(book);
        }
    }
}
