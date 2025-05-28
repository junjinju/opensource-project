package com.example.backend.repository;

import com.example.backend.entity.Recipe;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByUser(User user);
    Optional<Recipe> findByRecipeIdAndUser(Long recipeId, User user);
}