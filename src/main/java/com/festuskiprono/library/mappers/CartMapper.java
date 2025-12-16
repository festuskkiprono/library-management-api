package com.festuskiprono.library.mappers;

import com.festuskiprono.library.dtos.CartDto;
import com.festuskiprono.library.dtos.CartItemDto;
import com.festuskiprono.library.dtos.CartProductDto;
import com.festuskiprono.library.entities.Book;
import com.festuskiprono.library.entities.Cart;
import com.festuskiprono.library.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "items", source = "items")
    CartDto toDto(Cart cart);

    @Mapping(target = "product", source = "book")
    @Mapping(target = "quantity", source = "copies")
    CartItemDto toItemDto(CartItem cartItem);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "title")
    CartProductDto toProductDto(Book book);
}
