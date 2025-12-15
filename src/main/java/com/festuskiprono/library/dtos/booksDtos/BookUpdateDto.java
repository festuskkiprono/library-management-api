package com.festuskiprono.library.dtos.booksDtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class BookUpdateDto {
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 255, message = "Author must not exceed 255 characters")
    private String author;

    @Size(max = 20, message = "ISBN must not exceed 20 characters")
    private String isbn;

    private String description;

    private Short genreId;

    @Min(value = 0, message = "Total copies must be at least 0")
    private Integer totalCopies;

    @Min(value = 0, message = "Available copies must be at least 0")
    private Integer availableCopies;
}
