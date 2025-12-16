package com.festuskiprono.library.mappers;

import com.festuskiprono.library.dtos.CartDto;
import com.festuskiprono.library.entities.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);
}
