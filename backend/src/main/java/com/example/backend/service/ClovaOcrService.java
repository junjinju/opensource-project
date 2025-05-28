package com.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClovaOcrService {

    private final RestTemplate restTemplate;

    @Value("${clova.ocr.invoke-url}")
    private String invokeUrl;

    @Value("${clova.ocr.secret-key}")
    private String secretKey;

    public String callOcrApi(String imageUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-OCR-SECRET", secretKey);

        // body 구성
        Map<String, Object> image = Map.of("format", "jpg", "url", imageUrl, "name", "receipt");
        Map<String, Object> requestBody = Map.of("images", List.of(image), "requestId", UUID.randomUUID().toString(), "version", "V2", "timestamp", System.currentTimeMillis());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(invokeUrl, requestEntity, String.class);
        return response.getBody();
    }
}
