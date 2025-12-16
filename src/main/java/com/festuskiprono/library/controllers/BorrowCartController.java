package com.festuskiprono.library.controllers;

import com.festuskiprono.library.dtos.CartDto;
import com.festuskiprono.library.entities.Cart;
import com.festuskiprono.library.mappers.CartMapper;
import com.festuskiprono.library.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class BorrowCartController {
    private final CartRepository borrowCartRepository;
    private final CartMapper cartMapper;



    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder)
    {
        var cart = new Cart();
         borrowCartRepository.save(cart);
         var cartDto = cartMapper.toDto(cart);
         var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
         return ResponseEntity.created(uri).body(cartDto);

    }
}
