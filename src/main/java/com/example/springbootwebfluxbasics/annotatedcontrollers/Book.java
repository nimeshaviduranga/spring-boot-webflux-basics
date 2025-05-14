package com.example.springbootwebfluxbasics.annotatedcontrollers;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String id;

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotNull(message = "Year is required")
    @Min(value = 1000, message = "Year must be after 1000")
    private Integer year;

    private String genre;
    private String description;

    @Min(value = 0, message = "Price cannot be negative")
    private Double price;

    private Boolean available;
}