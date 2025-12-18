package com.festuskiprono.library.controllers;

import com.festuskiprono.library.dtos.BorrowTransactionDto;
import com.festuskiprono.library.dtos.CheckoutRequest;
import com.festuskiprono.library.entities.BorrowTransaction;
import com.festuskiprono.library.mappers.BorrowTransactionMapper;
import com.festuskiprono.library.repositories.BorrowTransactionRepository;
import com.festuskiprono.library.services.BorrowTransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/borrow")
@AllArgsConstructor
public class BorrowController {

    private final BorrowTransactionService transactionService;
    private final BorrowTransactionRepository transactionRepository;
    private final BorrowTransactionMapper transactionMapper;



    //Convert cart items into a borrow transaction
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(
            @Valid @RequestBody CheckoutRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        try {
            BorrowTransaction transaction = transactionService.createBorrowTransaction(
                    request.getCartId(),
                    request.getUserId(),
                    request.getBorrowDays()
            );

            BorrowTransactionDto dto = transactionMapper.toDto(transaction);

            var uri = uriBuilder
                    .path("/borrow/{id}")
                    .buildAndExpand(dto.getId())
                    .toUri();

            return ResponseEntity.created(uri).body(dto);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }


      //GET /borrow/{transactionId}
     //Get details of a specific borrow transaction

    @GetMapping("/{transactionId}")
    public ResponseEntity<BorrowTransactionDto> getTransaction(@PathVariable Long transactionId) {
        BorrowTransaction transaction = transactionRepository.findById(transactionId)
                .orElse(null);

        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(transactionMapper.toDto(transaction));
    }

    //GET /borrow/user/{userId}
    // Get all borrow transactions for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BorrowTransactionDto>> getUserTransactions(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly
    ) {
        List<BorrowTransaction> transactions;

        if (activeOnly) {
            transactions = transactionRepository.findByUserIdAndReturnDateIsNull(userId);
        } else {
            transactions = transactionRepository.findByUserId(userId);
        }

        return ResponseEntity.ok(transactionMapper.toDtoList(transactions));
    }

    //PUT /borrow/{transactionId}/return
    //Mark books as returned and update inventory
    @PutMapping("/{transactionId}/return")
    public ResponseEntity<?> returnBooks(@PathVariable Long transactionId) {
        try {
            BorrowTransaction transaction = transactionService.returnBooks(transactionId);
            return ResponseEntity.ok(transactionMapper.toDto(transaction));

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}