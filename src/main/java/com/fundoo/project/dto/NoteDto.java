package com.fundoo.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NoteDto {
    @NotBlank(message = "Title can't be empty")
    private String title;
    private String description;
    private String color;
}
