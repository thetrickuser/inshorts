package com.inshorts.newsapi.model;

import lombok.Data;

import java.util.List;

@Data
public class OpenRouterResponse {

    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;

}
