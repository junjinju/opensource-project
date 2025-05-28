package com.example.backend.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RecipeResponse {
    private Long recipeId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}

