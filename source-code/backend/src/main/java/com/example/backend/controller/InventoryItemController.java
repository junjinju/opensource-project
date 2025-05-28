package com.example.backend.controller;

import com.example.backend.dto.request.InventoryItemRequest;
import com.example.backend.dto.request.InventoryItemUpdateRequest;
import com.example.backend.dto.response.InventoryItemResponse;
import com.example.backend.service.InventoryItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    @GetMapping("/api/inventories/{inventoryId}/items")
    public ResponseEntity<List<InventoryItemResponse>> getItems(
            @PathVariable Long inventoryId) {
        return ResponseEntity.ok(inventoryItemService.getItemsByInventory(inventoryId));
    }

    @PostMapping("/api/inventory-items")
    public ResponseEntity<InventoryItemResponse> createItem(
            @RequestBody InventoryItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventoryItemService.createItem(request));
    }

    @PutMapping("/api/inventory-items/{itemId}")
    public ResponseEntity<?> updateItem(
            @PathVariable Long itemId,
            @RequestBody InventoryItemUpdateRequest request) {
        return ResponseEntity.ok(inventoryItemService.updateItem(itemId, request));
    }

    @DeleteMapping("/api/inventory-items/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId) {
        inventoryItemService.deleteItem(itemId);
        return ResponseEntity.ok(Map.of("message", "Item deleted"));
    }
}

