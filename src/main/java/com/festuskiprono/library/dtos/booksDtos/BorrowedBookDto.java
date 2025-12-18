/*
package com.festuskiprono.library.dtos.booksDtos;

public class BorrowedBookDto {
    public String author;
    public String title;
    public String isbn;
    public String description;
}
*/

package com.festuskiprono.library.dtos.booksDtos;

import lombok.Data;

@Data
public class BorrowedBookDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Short genreId;
    private String genreName;
    private Integer availableCopies;
    private Integer totalCopies;
}