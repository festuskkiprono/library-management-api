package com.festuskiprono.library.repositories;

import com.festuskiprono.library.entities.Genre;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GenreRepository extends JpaRepository<Genre, Short> {
}
