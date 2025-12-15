package com.festuskiprono.library.controllers;

import com.festuskiprono.library.dtos.genreDtos.GenreCreateDto;
import com.festuskiprono.library.dtos.genreDtos.GenreDto;
import com.festuskiprono.library.dtos.genreDtos.GenreUpdateDto;
import com.festuskiprono.library.entities.Genre;
import com.festuskiprono.library.mappers.GenreMapper;
import com.festuskiprono.library.repositories.GenreRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/genre")
public class GenreController {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @GetMapping
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        List<GenreDto> genreDtos = genres.stream()
                .map(genreMapper::toDto)
                .toList();
        return ResponseEntity.ok(genreDtos);
    }

    @PostMapping
    public ResponseEntity<GenreDto> createGenre(@Valid @RequestBody GenreCreateDto genreCreateDto) {
        Genre genre = genreMapper.toEntity(genreCreateDto);
        Genre savedGenre = genreRepository.save(genre);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(genreMapper.toDto(savedGenre));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreDto> updateGenre(
            @PathVariable Short id,
            @Valid @RequestBody GenreUpdateDto genreUpdateDto) {

        if (!genreRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Genre existingGenre = genreRepository.findById(id).orElseThrow();
        genreMapper.updateEntityFromDto(genreUpdateDto, existingGenre);
        Genre updatedGenre = genreRepository.save(existingGenre);

        return ResponseEntity.ok(genreMapper.toDto(updatedGenre));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Short id) {
        if (!genreRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        genreRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}