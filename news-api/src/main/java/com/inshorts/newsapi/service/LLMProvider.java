package com.inshorts.newsapi.service;

import com.inshorts.newsapi.model.LlmIntentResponse;

public interface LLMProvider {

    String processQuery(String query);
    LlmIntentResponse parseResponse(String response);

}
