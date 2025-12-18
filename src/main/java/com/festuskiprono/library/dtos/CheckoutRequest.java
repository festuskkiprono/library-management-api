package com.festuskiprono.library.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequest {
    @NotNull(message = "Cart ID is required")
    private UUID cartId;

    @NotNull(message = "User ID is required")
    private Long userId;

    private Integer borrowDays = 14; //14 days borrowing period
}