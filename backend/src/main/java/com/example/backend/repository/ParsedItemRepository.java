package com.example.backend.repository;

import com.example.backend.entity.ParsedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParsedItemRepository extends JpaRepository<ParsedItem, Long> {
    List<ParsedItem> findByReceipt_ReceiptId(Long receiptId);
}

