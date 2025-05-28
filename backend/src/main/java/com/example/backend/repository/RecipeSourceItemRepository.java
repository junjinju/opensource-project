package com.example.backend.repository;

import com.example.backend.entity.RecipeSourceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeSourceItemRepository extends JpaRepository<RecipeSourceItem, Long> {
}
