package com.example.backend.service;

import com.example.backend.dto.response.ParsedItemResponse;
import com.example.backend.dto.response.ReceiptUploadResponse;
import com.example.backend.entity.ParsedItem;
import com.example.backend.entity.Receipt;
import com.example.backend.entity.User;
import com.example.backend.repository.ParsedItemRepository;
import com.example.backend.repository.ReceiptRepository;
import com.example.backend.util.FirebaseUploader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ParsedItemRepository parsedItemRepository;
    private final FirebaseUploader firebaseUploader;
    private final ClovaOcrService clovaOcrService;

    public ReceiptUploadResponse uploadReceipt(MultipartFile image, User user) {
        try {
            // 1. Firebase 업로드
            String imageUrl = firebaseUploader.uploadFile(image, "receipts");

            // 2. DB 저장
            Receipt receipt = new Receipt();
            receipt.setUser(user);
            receipt.setImageUrl(imageUrl);
            receipt = receiptRepository.save(receipt);

            // 3. OCR API 호출
            String ocrJson = clovaOcrService.callOcrApi(imageUrl);

            // 로그 출력
            System.out.println("OCR 결과 JSON:\n" + ocrJson);

            // 4. OCR 결과 파싱
            List<ParsedItem> items = parseOcrJson(ocrJson, receipt);
            parsedItemRepository.saveAll(items);

            // 5. 응답 반환
            return new ReceiptUploadResponse(receipt.getReceiptId(), receipt.getUploadedAt());

        } catch (Exception e) {
            throw new RuntimeException("영수증 업로드 중 오류 발생", e);
        }
    }

    private List<ParsedItem> parseOcrJson(String ocrJson, Receipt receipt) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(ocrJson);

        List<ParsedItem> itemList = new ArrayList<>();
        JsonNode fields = root.path("images").get(0).path("fields");

        // 전체 inferText 추출
        List<String> texts = new ArrayList<>();
        for (JsonNode field : fields) {
            String text = field.path("inferText").asText().trim();
            if (!text.isEmpty()) {
                texts.add(text);
            }
        }

        // 1. 품목 블록 단위로 분리 (예: 001, 002, ...)
        List<List<String>> blocks = new ArrayList<>();
        List<String> currentBlock = new ArrayList<>();

        for (String text : texts) {
            if (text.matches("^\\d{3}$")) {  // 품목 번호 시작
                if (!currentBlock.isEmpty()) {
                    blocks.add(new ArrayList<>(currentBlock));
                    currentBlock.clear();
                }
            }
            currentBlock.add(text);
        }
        if (!currentBlock.isEmpty()) {
            blocks.add(currentBlock);
        }

        // 2. 각 블록에서 품목명, 수량 추출
        for (List<String> block : blocks) {
            if (block.isEmpty()) continue;

            StringBuilder itemNameBuilder = new StringBuilder();
            Integer quantity = null;

            for (int i = 1; i < block.size(); i++) { // 0번째는 001, 002 같은 품목 번호
                String word = block.get(i);

                // 수량 후보 (1~99)
                if (quantity == null && word.matches("^\\d{1,2}$")) {
                    quantity = Integer.parseInt(word);
                }

                // 바코드 (8자리 이상 숫자 or *숫자)
                else if (word.matches("^\\*?\\d{8,}$")) {
                    continue; // 건너뜀
                }

                // 가격 패턴은 무시
                else if (word.matches(".*\\d+.*원$") || word.matches("^[0-9,]+$")) {
                    continue;
                }

                // 단위 정보는 포함
                else {
                    String cleaned = word.replaceAll("^\\*", "")   // * 제거
                            .replaceAll("^P", "");   // P 제거
                    itemNameBuilder.append(cleaned).append(" ");
                }
            }

            String finalItemName = itemNameBuilder.toString().trim();
            if (!finalItemName.isEmpty()) {
                itemList.add(new ParsedItem(null, receipt, finalItemName, quantity != null ? quantity : 1));
            }
        }

        return itemList;
    }

    public List<ParsedItemResponse> getParsedItems(Long receiptId) {
        return parsedItemRepository.findByReceipt_ReceiptId(receiptId).stream()
                .map(item -> new ParsedItemResponse(item.getParsedItemId(), item.getItemName(), item.getQuantity()))
                .collect(Collectors.toList());
    }
}
