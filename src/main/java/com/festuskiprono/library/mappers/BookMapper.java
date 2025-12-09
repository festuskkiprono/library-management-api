package com.festuskiprono.library.mappers;

import com.festuskiprono.library.entities.Book;
import com.festuskiprono.library.dtos.BorrowedBookDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface BookMapper {

    BorrowedBookDto  toDto(Book book);
    List<BorrowedBookDto> toDtoList(List<Book> books);
}
