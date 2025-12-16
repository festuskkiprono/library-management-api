package com.festuskiprono.library.repositories;

import com.festuskiprono.library.entities.Book;
import com.festuskiprono.library.entities.Cart;
import com.festuskiprono.library.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository  extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndBook(Cart cart, Book book);
}
