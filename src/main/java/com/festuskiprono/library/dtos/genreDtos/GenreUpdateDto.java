package com.festuskiprono.library.dtos.genreDtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GenreUpdateDto {
    @Size(max = 100, message = "Genre name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}
