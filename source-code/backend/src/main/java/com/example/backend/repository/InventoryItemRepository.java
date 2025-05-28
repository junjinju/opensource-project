package com.example.backend.repository;

import com.example.backend.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    List<InventoryItem> findByInventory_InventoryId(Long inventoryId);
}
