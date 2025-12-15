package com.festuskiprono.library.mappers;

import com.festuskiprono.library.dtos.genreDtos.GenreCreateDto;
import com.festuskiprono.library.dtos.genreDtos.GenreDto;
import com.festuskiprono.library.dtos.genreDtos.GenreUpdateDto;

import com.festuskiprono.library.entities.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreDto toDto(Genre genre);

    List<GenreDto> toDtoList(List<Genre> genres);

    @Mapping(target = "id", ignore = true)
    Genre toEntity(GenreCreateDto genreCreateDto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(GenreUpdateDto genreUpdateDto, @MappingTarget Genre existingGenre);
}
