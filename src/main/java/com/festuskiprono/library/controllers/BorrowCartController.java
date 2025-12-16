package com.festuskiprono.library.controllers;

import com.festuskiprono.library.dtos.AddBookToCartRequest;
import com.festuskiprono.library.dtos.CartDto;
import com.festuskiprono.library.dtos.CartItemDto;
import com.festuskiprono.library.entities.Cart;
import com.festuskiprono.library.entities.CartItem;
import com.festuskiprono.library.mappers.CartMapper;
import com.festuskiprono.library.repositories.BookRepository;
import com.festuskiprono.library.repositories.CartItemRepository;
import com.festuskiprono.library.repositories.CartRepository;
import com.festuskiprono.library.services.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class BorrowCartController {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder)
    {
        var cart = new Cart();
        cartRepository.save(cart);
         var cartDto = cartMapper.toDto(cart);
         var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
         return ResponseEntity.created(uri).body(cartDto);

    }
    @PostMapping("/{cartId}/items")
    public ResponseEntity<?> addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddBookToCartRequest request) {

        // Find cart
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        // Find book
        var book = bookRepository.findById(request.getBookId()).orElse(null);
        if (book == null) {
            return ResponseEntity.badRequest().build();
        }

        // Check if book already exists in cart
        var existingCartItem = cartItemRepository.findByCartAndBook(cart, book);

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "message", "You can only borrow one copy of this book."
                    ));
        }
        else {
            // New book, create new cart item
            cartItem = new CartItem();
            cartItem.setBook(book);
            cartItem.setCopies(1);
            cartItem.setCart(cart);
        }

        // Save the cart item directly and get the saved entity with ID
        var savedCartItem = cartItemRepository.save(cartItem);

        // Map to DTO (now the ID will be present)
        var cartItemDto = cartMapper.toItemDto(savedCartItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }
    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        var cartDto = cartMapper.toDto(cart);
        return ResponseEntity.ok(cartDto);
    }
    @DeleteMapping("/{cartId}/items/{bookId}")
    public ResponseEntity<?> removeBookFromCart(
            @PathVariable UUID cartId,
            @PathVariable Long bookId
    ) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Cart was not found"));
        }

        boolean removed = cart.removeItem(bookId);

        if (!removed) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Book is not in the cart"));
        }
        bookService.returnBook(bookId.intValue());
        cartRepository.save(cart);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        bookService.returnBooksFromCart(cart.getItems());

        cart.clear();
        cartRepository.save(cart);

        return ResponseEntity.noContent().build();
    }

}
