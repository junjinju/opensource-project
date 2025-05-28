package com.example.backend.dto.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParsedItemResponse {
    private Long parsedItemId;
    private String itemName;
    private int quantity;
}
