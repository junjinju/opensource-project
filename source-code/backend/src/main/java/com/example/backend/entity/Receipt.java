package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Receipt {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receiptId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @CreationTimestamp
    private LocalDateTime uploadedAt;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
    private List<ParsedItem> parsedItems = new ArrayList<>();
}

