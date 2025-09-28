package com.inshorts.newsapi.service;

import com.inshorts.newsapi.model.OpenRouterResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenRouterClient {

    @Value("${openrouter.apiKey}")
    private String apiKey;

    @Value("${openrouter.modelId}")  // e.g. "mistralai/mixtral-8x7b-instruct:free"
    private String modelId;

    private RestTemplate rest = new RestTemplate();

    public String callModel(String prompt) {
        String url = "https://openrouter.ai/api/v1/chat/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Define body according to their spec
        Map<String, Object> body = new HashMap<>();
        body.put("model", modelId);
        // You might need a messages field (if chat style) or prompt style
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role","user","content", prompt));
        body.put("messages", messages);
        // other params if needed: max_tokens, temperature etc.

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<OpenRouterResponse> resp = rest.postForEntity(url, request, OpenRouterResponse.class);
        if (resp.getStatusCode() == HttpStatus.OK && resp.getBody() != null) {
            return resp.getBody().getChoices().get(0).getMessage().getContent();
        } else {
            throw new RuntimeException("OpenRouter call failed: " + resp.getStatusCode());
        }
    }
}
