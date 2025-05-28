package com.example.backend.controller;

import com.example.backend.dto.request.RecipeItemRequest;
import com.example.backend.dto.response.RecipeListResponse;
import com.example.backend.dto.response.RecipeResponse;
import com.example.backend.entity.User;
import com.example.backend.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<RecipeResponse> createRecipe(
            @RequestBody List<RecipeItemRequest> items,
            @AuthenticationPrincipal User user) {
        RecipeResponse response = recipeService.createRecipe(items, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<RecipeListResponse>> getRecipes(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(recipeService.getRecipes(user));
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeResponse> getRecipeDetail(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(recipeService.getRecipeDetail(recipeId, user));
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Map<String, String>> deleteRecipe(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal User user) {
        recipeService.deleteRecipe(recipeId, user);
        return ResponseEntity.ok(Map.of("message", "레시피가 성공적으로 삭제되었습니다."));
    }
}