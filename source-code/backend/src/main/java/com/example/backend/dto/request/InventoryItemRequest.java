package com.example.backend.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemRequest {
    private Long inventoryId;
    private String itemName;
    private Integer quantity;
    private String storageType;
    private LocalDate expirationDate;
}
