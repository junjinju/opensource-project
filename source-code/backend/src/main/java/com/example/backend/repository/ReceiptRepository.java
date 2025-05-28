package com.example.backend.repository;

import com.example.backend.entity.Receipt;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findByUser(User user);
}

