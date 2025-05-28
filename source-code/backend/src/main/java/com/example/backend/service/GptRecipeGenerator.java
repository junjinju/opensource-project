package com.example.backend.service;

import com.example.backend.dto.request.RecipeItemRequest;
import com.example.backend.entity.Recipe;
import com.example.backend.entity.User;
import com.example.backend.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GptRecipeGenerator {

    private final RestTemplate restTemplate = new RestTemplate();
    private final RecipeRepository recipeRepository;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.organization.id}")
    private String openAiOrgId;

    public Recipe generateAndSaveRecipe(List<RecipeItemRequest> items, User user) {

        // 1. Prompt 구성
        String itemList = items.stream()
                .map(i -> i.getItemName() + " " + i.getQuantity() + "개")
                .collect(Collectors.joining(", "));

        String prompt = """
아래 나열된 재료를 활용해서 한국 가정식 스타일의 현실적이고 먹을 수 있는 요리 하나를 추천해 주세요.

반드시 아래 형식을 지켜 주세요:
제목: <요리 제목>
레시피 설명: <간단하고 현실적인 설명, 최대 10문장 이내>

조건:
- 반드시 아래 재료만 사용하고, 존재하지 않는 재료는 절대 포함하지 마세요.
- 일반적인 요리 상식에 어긋나는 조합(예: 우유 초밥, 치즈 국밥 등)은 피하세요.
- 조리법은 한국 가정식 기준으로 자연스럽고 일반적인 수준이어야 합니다.
- 재료의 조리법(굽기, 볶기, 끓이기 등)을 고려해서 자연스러운 요리를 생성해 주세요.

재료: %s
""".formatted(itemList);

        // 2. GPT 요청 구성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
        headers.set("OpenAI-Organization", openAiOrgId);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7,
                "max_tokens", 400
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions",
                    entity,
                    Map.class
            );

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            String content = (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");

            // 3. GPT 응답 파싱 → 제목 / 설명 분리
            String[] parts = content.split("\n", 2); // 첫 줄: 제목, 나머지: 설명
            String title = parts.length > 0 ? parts[0].replaceAll("^[0-9.\\-\\s]*", "") : "추천 요리";
            String recipeContent = parts.length > 1 ? parts[1].trim() : "레시피 내용 없음";

            // 4. DB 저장
            Recipe recipe = Recipe.builder()
                    .user(user)
                    .title(title)
                    .content(recipeContent)
                    .build();

            return recipeRepository.save(recipe);

        } catch (HttpClientErrorException.TooManyRequests e) {
            throw new RuntimeException("GPT 호출 실패: 사용량 한도 초과", e);
        } catch (Exception e) {
            throw new RuntimeException("GPT 호출 실패: " + e.getMessage(), e);
        }
    }
}