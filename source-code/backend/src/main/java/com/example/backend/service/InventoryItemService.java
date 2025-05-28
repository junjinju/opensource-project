package com.example.backend.service;

import com.example.backend.dto.request.InventoryItemRequest;
import com.example.backend.dto.request.InventoryItemUpdateRequest;
import com.example.backend.dto.response.InventoryItemResponse;
import com.example.backend.entity.Inventory;
import com.example.backend.entity.InventoryItem;
import com.example.backend.entity.StorageType;
import com.example.backend.repository.InventoryItemRepository;
import com.example.backend.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryItemService {

    private final InventoryItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;

    public InventoryItemResponse createItem(InventoryItemRequest request) {
        Inventory inventory = inventoryRepository.findById(request.getInventoryId())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        InventoryItem item = new InventoryItem();
        item.setInventory(inventory);
        item.setItemName(request.getItemName());
        item.setQuantity(request.getQuantity());
        item.setStorageType(StorageType.valueOf(request.getStorageType()));
        item.setExpirationDate(request.getExpirationDate());

        item = itemRepository.save(item);
        return toResponse(item);
    }

    public List<InventoryItemResponse> getItemsByInventory(Long inventoryId) {
        return itemRepository.findByInventory_InventoryId(inventoryId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public InventoryItemResponse updateItem(Long itemId, InventoryItemUpdateRequest request) {
        InventoryItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setItemName(request.getItemName());
        item.setQuantity(request.getQuantity());
        item.setStorageType(StorageType.valueOf(request.getStorageType()));
        item.setExpirationDate(request.getExpirationDate());

        return toResponse(itemRepository.save(item));
    }

    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    private InventoryItemResponse toResponse(InventoryItem item) {
        return new InventoryItemResponse(
                item.getItemId(),
                item.getItemName(),
                item.getQuantity(),
                item.getStorageType().name(),
                item.getExpirationDate()
        );
    }
}

