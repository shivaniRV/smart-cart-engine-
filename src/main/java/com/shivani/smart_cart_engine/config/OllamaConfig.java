
package com.shivani.smart_cart_engine.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OllamaConfig {

    @Bean
    public ChatModel chatModel() {

        return OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("qwen2.5:0.5b")
                .temperature(0.3)
                .timeout(Duration.ofSeconds(120))
                .build();
    }
}
