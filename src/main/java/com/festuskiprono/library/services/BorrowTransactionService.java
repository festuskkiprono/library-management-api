package com.festuskiprono.library.services;

import com.festuskiprono.library.entities.*;
import com.festuskiprono.library.repositories.BorrowTransactionRepository;
import com.festuskiprono.library.repositories.CartRepository;
import com.festuskiprono.library.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BorrowTransactionService {

    private final BorrowTransactionRepository transactionRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BookService bookService;

    @Transactional
    public BorrowTransaction createBorrowTransaction(UUID cartId, Long userId, Integer borrowDays) {
        // 1. Get cart with items
        Cart cart = cartRepository.getCartWithItems(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        System.out.println(cart);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout an empty cart");
        }

        // 2. Get user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Create borrow transaction
        BorrowTransaction transaction = new BorrowTransaction();
        transaction.setUser(user);
        transaction.setStatus("ACTIVE");
        transaction.setBorrowDate(LocalDate.now());
        transaction.setDueDate(LocalDate.now().plusDays(borrowDays));
        transaction.setReturnDate(null); // Not returned yet

        // 4. Convert cart items to borrowed books

        for (CartItem cartItem : cart.getItems()) {
            BorrowedBook borrowedBook = new BorrowedBook();
            borrowedBook.setBook(cartItem.getBook());
            borrowedBook.setBorrowTransaction(transaction); // OWNING SIDE
            transaction.getBorrowedBooks().add(borrowedBook); // INVERSE SIDE
        }



        // 5. Save transaction
        BorrowTransaction savedTransaction = transactionRepository.save(transaction);

        // 6. Clear cart
        cart.clear();
        cartRepository.save(cart);

        return savedTransaction;
    }

    @Transactional
    public BorrowTransaction returnBooks(Long transactionId) {
        BorrowTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getReturnDate() != null) {
            throw new RuntimeException("Books already returned");
        }

        // Set return date
        transaction.setReturnDate(LocalDate.now());
        transaction.setStatus("RETURNED");

        // Return books to inventory
        for (BorrowedBook borrowedBook : transaction.getBorrowedBooks()) {
            bookService.returnBook(borrowedBook.getBook().getId().intValue());
        }

        return transactionRepository.save(transaction);
    }
}