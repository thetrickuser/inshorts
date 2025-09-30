package com.inshorts.newsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LlmIntentResponse {
    private List<String> intent;           // "category", "source", "search", "nearby", "score"
    private List<String> entities;   // ["Elon Musk", "Twitter", "Palo Alto"]
}
