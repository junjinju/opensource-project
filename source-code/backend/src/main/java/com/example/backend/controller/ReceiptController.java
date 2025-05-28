package com.example.backend.controller;

import com.example.backend.dto.response.ParsedItemResponse;
import com.example.backend.dto.response.ReceiptUploadResponse;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.ReceiptService;
import com.example.backend.util.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ReceiptUploadResponse> uploadReceipt(
            @RequestParam("image") MultipartFile image,
            HttpServletRequest request
    ) {
        // 1. 토큰 추출 및 검증
        String token = jwtProvider.resolveToken(request);
        if (token == null || !jwtProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2. 토큰에서 이메일 추출
        String email = jwtProvider.getEmail(token);

        // 3. 이메일로 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 4. 서비스 호출
        ReceiptUploadResponse response = receiptService.uploadReceipt(image, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{receiptId}/parsed-items")
    public ResponseEntity<List<ParsedItemResponse>> getParsedItems(@PathVariable Long receiptId) {
        List<ParsedItemResponse> items = receiptService.getParsedItems(receiptId);
        return ResponseEntity.ok(items);
    }
}

