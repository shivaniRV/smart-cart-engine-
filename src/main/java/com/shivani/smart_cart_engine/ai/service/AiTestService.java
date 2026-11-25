package com.shivani.smart_cart_engine.ai.service;

import dev.langchain4j.model.chat.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class AiTestService {

    private final ChatModel chatModel;

    public AiTestService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String askAI(String question) {
        return chatModel.chat(question);
    }
}