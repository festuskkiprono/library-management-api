package com.festuskiprono.library.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CartDto {
    public UUID id;
    private List<CartItemDto> items = new ArrayList<CartItemDto>();

}
