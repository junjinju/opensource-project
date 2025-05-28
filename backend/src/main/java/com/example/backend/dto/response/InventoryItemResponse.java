package com.example.backend.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemResponse {
    private Long itemId;
    private String itemName;
    private Integer quantity;
    private String storageType;
    private LocalDate expirationDate;
}
