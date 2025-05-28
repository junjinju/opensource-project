package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ParsedItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parsedItemId;

    @ManyToOne
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;

    private String itemName;

    private Integer quantity;
}

