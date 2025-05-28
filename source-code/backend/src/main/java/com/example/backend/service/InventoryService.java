package com.example.backend.service;

import com.example.backend.dto.request.InventoryCreateRequest;
import com.example.backend.dto.request.InventoryUpdateRequest;
import com.example.backend.dto.response.InventoryResponse;
import com.example.backend.entity.Inventory;
import com.example.backend.entity.User;
import com.example.backend.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryResponse createInventory(InventoryCreateRequest request, User user) {
        Inventory inventory = new Inventory();
        inventory.setUser(user);
        inventory.setInventoryName(request.getInventoryName());

        inventory = inventoryRepository.save(inventory);
        return new InventoryResponse(inventory.getInventoryId(), inventory.getInventoryName());
    }

    public List<InventoryResponse> getInventories(User user) {
        return inventoryRepository.findByUser(user).stream()
                .map(inv -> new InventoryResponse(inv.getInventoryId(), inv.getInventoryName()))
                .collect(Collectors.toList());
    }

    public InventoryResponse updateInventory(Long inventoryId, InventoryUpdateRequest request, User user) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventory.setInventoryName(request.getInventoryName());
        inventory.setUser(user);

        inventoryRepository.save(inventory);
        return new InventoryResponse(inventory.getInventoryId(), inventory.getInventoryName());
    }

    public void deleteInventory(Long inventoryId, User user) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        if (!inventory.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("해당 냉장고에 대한 삭제 권한이 없습니다.");
        }

        inventoryRepository.delete(inventory);
    }
}

