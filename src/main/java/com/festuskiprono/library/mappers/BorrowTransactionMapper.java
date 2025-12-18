package com.festuskiprono.library.mappers;

import com.festuskiprono.library.dtos.BorrowTransactionDto;
import com.festuskiprono.library.dtos.TransactedBookDto;
import com.festuskiprono.library.dtos.booksDtos.BorrowedBookDto;
import com.festuskiprono.library.entities.BorrowTransaction;
import com.festuskiprono.library.entities.BorrowedBook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BorrowTransactionMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "borrowedBooks", target = "books")
    @Mapping(expression = "java(transaction.getBorrowedBooks().size())", target = "totalBooks")
    @Mapping(expression = "java(determineStatus(transaction))", target = "status")
    BorrowTransactionDto toDto(BorrowTransaction transaction);

    List<BorrowTransactionDto> toDtoList(List<BorrowTransaction> transactions);

    @Mapping(source = "book.id", target = "id")
    @Mapping(source = "book.title", target = "title")
    @Mapping(source = "book.author", target = "author")
    @Mapping(source = "book.isbn", target = "isbn")
    @Mapping(source = "book.genre.id", target = "genreId")
    @Mapping(source = "book.genre.name", target = "genreName")
    @Mapping(source = "book.availableCopies", target = "availableCopies")
    @Mapping(source = "book.totalCopies", target = "totalCopies")
    BorrowedBookDto borrowedBookToDto(BorrowedBook borrowedBook);

    TransactedBookDto toTransactedBook(BorrowedBook borrowedBook);


    default String determineStatus(BorrowTransaction transaction) {
        if (transaction.getReturnDate() != null) {
            return "RETURNED";
        }
        if (transaction.getDueDate().isBefore(java.time.LocalDate.now())) {
            return "OVERDUE";
        }
        return "ACTIVE";
    }
}