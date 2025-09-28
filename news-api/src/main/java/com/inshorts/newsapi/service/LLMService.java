package com.inshorts.newsapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inshorts.newsapi.model.LlmIntentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LLMService {

    private final OpenRouterClient openRouterClient;

    public LlmIntentResponse processQuery(String userQuery) {
        String prompt = """
            You are a system that extracts structured JSON with:
            - "intent": one of ["category","source","search","nearby","score"]
            - "entities": list of important names, places, topics.

            Return ONLY JSON. No explanations.

            Example:
            {"intent":"nearby","entities":["Elon Musk","Twitter","Palo Alto"]}

            QUERY: %s
            """.formatted(userQuery);

        String rawResponse = openRouterClient.callModel(prompt);

        // Parse JSON back into LlmIntentResponse
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(rawResponse, LlmIntentResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse LLM response: " + rawResponse, e);
        }
    }

    public String summarizeArticle(String articleText) {
        String prompt = """
            Summarize the following article in 2–3 sentences, focusing on the main news event.
            Return plain text only.

            Article:
            %s
            """.formatted(articleText);

        return openRouterClient.callModel(prompt);
    }
}
