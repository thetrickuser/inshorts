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
                You are an assistant that analyzes news queries.
                
                         Task:
                         - Extract named entities (people, organizations, locations, events).
                         - Identify the user's intent as one of ["category", "source", "nearby", "search"].
                         - When intent is "nearby", ensure entities include location names.
                         - Return output in strict JSON format as such:
                
                         {
                           "entities": [list of extracted entities],
                           "intent": [list of inferred intents]
                         }
                
                         Now analyze the given query and respond only with the JSON object. 
                         Query: "%s"
                
            """.formatted(userQuery).stripIndent();

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
