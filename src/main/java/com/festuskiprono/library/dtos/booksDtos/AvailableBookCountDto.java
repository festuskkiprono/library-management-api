package com.festuskiprono.library.dtos.booksDtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AvailableBookCountDto {
    @JsonProperty("availableCopies")
    public int count;
    public String title;
    public String author;
    public String isbn;

}
