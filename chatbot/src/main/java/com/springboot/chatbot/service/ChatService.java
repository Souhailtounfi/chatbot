package com.springboot.chatbot.service;

import com.springboot.chatbot.dto.ChatRequest;
import com.springboot.chatbot.dto.ChatResponse;
import com.springboot.chatbot.dto.PromptRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ChatService {

    private final RestClient restClient;

    @Autowired
    public ChatService(RestClient restClient) {
        this.restClient = restClient;
    }

    @Value("${openapi.api.key}")
    private String apiKey;

    @Value("${openapi.api.model}")
    private String model;

    public String getChatResponse(PromptRequest promptrequest) {

        ChatRequest chatRequest = new ChatRequest(
                model,
                List.of(new ChatRequest.Message("user", promptrequest.prompt()))
        );

        ChatResponse response = restClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(chatRequest)
                .retrieve()
                .body(ChatResponse.class);
        return response.choices().get(0).message() .content();
    }
}
