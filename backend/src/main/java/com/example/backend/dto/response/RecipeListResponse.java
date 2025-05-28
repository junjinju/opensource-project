package com.example.backend.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RecipeListResponse {
    private Long recipeId;
    private String title;
    private LocalDateTime createdAt;
}
