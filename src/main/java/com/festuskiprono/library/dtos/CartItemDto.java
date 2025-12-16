package com.festuskiprono.library.dtos;

import lombok.Data;

@Data
public class CartItemDto {
    private Long id;
    private CartProductDto product;
    private int quantity;
}