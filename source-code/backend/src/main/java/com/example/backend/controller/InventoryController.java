package com.example.backend.controller;

import com.example.backend.dto.request.InventoryCreateRequest;
import com.example.backend.dto.request.InventoryUpdateRequest;
import com.example.backend.dto.response.InventoryResponse;
import com.example.backend.entity.User;
import com.example.backend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(
            @RequestBody InventoryCreateRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventoryService.createInventory(request, user));
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getInventories(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(inventoryService.getInventories(user));
    }

    @PutMapping("/{inventoryId}")
    public ResponseEntity<?> updateInventory(
            @PathVariable Long inventoryId,
            @RequestBody InventoryUpdateRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(inventoryService.updateInventory(inventoryId, request, user));
    }

    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<?> deleteInventory(
            @PathVariable Long inventoryId,
            @AuthenticationPrincipal User user) {
        inventoryService.deleteInventory(inventoryId, user);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}

