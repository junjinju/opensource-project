package com.example.backend.service;

import com.example.backend.dto.request.RecipeItemRequest;
import com.example.backend.dto.response.RecipeListResponse;
import com.example.backend.dto.response.RecipeResponse;
import com.example.backend.entity.Recipe;
import com.example.backend.entity.User;
import com.example.backend.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final GptRecipeGenerator gptRecipeGenerator;
    private final RecipeRepository recipeRepository;

    public RecipeResponse createRecipe(List<RecipeItemRequest> items, User user) {
        Recipe recipe = gptRecipeGenerator.generateAndSaveRecipe(items, user);
        return RecipeResponse.builder()
                .recipeId(recipe.getRecipeId())
                .title(recipe.getTitle())
                .content(recipe.getContent())
                .build();
    }

    public List<RecipeListResponse> getRecipes(User user) {
        return recipeRepository.findByUser(user).stream()
                .map(recipe -> RecipeListResponse.builder()
                        .recipeId(recipe.getRecipeId())
                        .title(recipe.getTitle()
                                .replaceFirst("(?i)^제목[:：\\s]*", "")
                                .replaceAll("[<>]", "")
                                .trim())
                        .createdAt(recipe.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public RecipeResponse getRecipeDetail(Long recipeId, User user) {
        Recipe recipe = recipeRepository.findByRecipeIdAndUser(recipeId, user)
                .orElseThrow(() -> new RuntimeException("해당 레시피를 찾을 수 없습니다."));
        return RecipeResponse.builder()
                .recipeId(recipe.getRecipeId())
                .title(recipe.getTitle())
                .content(recipe.getContent())
                .createdAt(recipe.getCreatedAt())
                .build();
    }

    public void deleteRecipe(Long recipeId, User user) {
        Recipe recipe = recipeRepository.findByRecipeIdAndUser(recipeId, user)
                .orElseThrow(() -> new RuntimeException("해당 레시피를 찾을 수 없습니다."));
        recipeRepository.delete(recipe);
    }
}