package com.fundoo.project.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "note")
@Data
public class NoteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long noteId;

    private String title;

    private String description;

    private String color;

    private boolean isPinned;

    private boolean isTrashed;

    private boolean isArchived;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdatedAt;

    private LocalDateTime reminder;
}
