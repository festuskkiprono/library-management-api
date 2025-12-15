package com.festuskiprono.library.mappers;

import com.festuskiprono.library.dtos.booksDtos.AvailableBookCountDto;
import com.festuskiprono.library.dtos.booksDtos.BookCreateDto;
import com.festuskiprono.library.entities.Book;
import com.festuskiprono.library.dtos.booksDtos.BorrowedBookDto;
import com.festuskiprono.library.dtos.booksDtos.BookUpdateDto;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel="spring")
public interface BookMapper {

    BorrowedBookDto  toDto(Book book);
    List<BorrowedBookDto> toDtoList(List<Book> books);

    @Mapping(target="id",ignore = true)
    void updateEntityFromDto(BookUpdateDto bookUpdateDto, @MappingTarget Book existingBook);

    Book toEntity(@Valid BookCreateDto bookCreateDto);

    @Mapping(source = "availableCopies", target = "count")
    AvailableBookCountDto toAvailableBookCountDto(Book book);
}
