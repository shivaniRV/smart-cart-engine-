
package com.shivani.smart_cart_engine.ai.service;

import com.shivani.smart_cart_engine.ai.dto.ProductCandidate;
import com.shivani.smart_cart_engine.ai.dto.ProductRecommendation;
import com.shivani.smart_cart_engine.ai.dto.RecommendationResponse;
import com.shivani.smart_cart_engine.ai.dto.ShoppingContext;
import com.shivani.smart_cart_engine.product.entity.Product;
import com.shivani.smart_cart_engine.product.repository.ProductRepository;

import dev.langchain4j.model.chat.ChatModel;
import org.springframework.stereotype.Service;

import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AiRecommendationService {

    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;
    private final ShoppingContextService shoppingContextService;
    private final ProductRepository productRepository;

    public AiRecommendationService(
            ChatModel chatModel,
            ObjectMapper objectMapper,
            ShoppingContextService shoppingContextService,
            ProductRepository productRepository) {

        this.chatModel = chatModel;
        this.objectMapper = objectMapper;
        this.shoppingContextService = shoppingContextService;
        this.productRepository = productRepository;
    }

    public RecommendationResponse getRecommendations(Long userId) {

        ShoppingContext context =
                shoppingContextService.buildShoppingContext(userId);

        try {
            String shoppingData =
                    objectMapper.writeValueAsString(context);

            String prompt = """
                    You are an intelligent e-commerce product recommendation assistant.

                    Analyze the following shopping data:

                    %s

                    Tasks:
                    1. Understand the user's shopping interests.
                    2. Recommend up to 3 relevant products.
                    3. Do not recommend products already purchased or present in the cart.
                    4. Select product IDs only from availableProducts.
                    5. Explain briefly why each product is recommended.

                    Return ONLY valid JSON in this exact format:

                    {
                      "recommendations": [
                        {
                          "productId": 3,
                          "reason": "Reason for recommending this product"
                        }
                      ]
                    }

                    Do not include Markdown code fences or additional text.
                    """.formatted(shoppingData);

            // Get response from the local AI model
            String aiResponse = chatModel.chat(prompt);

            // Remove Markdown code fences if the model adds them
            aiResponse = aiResponse.trim();

            if (aiResponse.startsWith("```")) {
                aiResponse = aiResponse.replaceFirst(
                        "(?s)^```(?:json)?\\s*",
                        ""
                );

                aiResponse = aiResponse.replaceFirst(
                        "\\s*```\\s*$",
                        ""
                );
            }

            // Parse the cleaned JSON response
            RecommendationResponse parsedResponse =
                    objectMapper.readValue(
                            aiResponse,
                            RecommendationResponse.class
                    );

            // Validate AI recommendations against database
            return validateRecommendations(parsedResponse, context);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to generate or process AI recommendations", e
            );
        }
    }

    private RecommendationResponse validateRecommendations(
            RecommendationResponse aiResponse,
            ShoppingContext context) {

        if (aiResponse == null ||
                aiResponse.getRecommendations() == null) {
            return new RecommendationResponse(List.of());
        }

        Set<Long> eligibleProductIds = context.getAvailableProducts()
                .stream()
                .map(ProductCandidate::getProductId)
                .collect(Collectors.toSet());

        Set<String> excludedProductNames = new HashSet<>();

        context.getCartProducts().stream()
                .filter(name -> name != null)
                .map(name -> name.toLowerCase(Locale.ROOT))
                .forEach(excludedProductNames::add);

        context.getPreviouslyPurchasedProducts().stream()
                .filter(name -> name != null)
                .map(name -> name.toLowerCase(Locale.ROOT))
                .forEach(excludedProductNames::add);

        List<ProductRecommendation> validRecommendations =
                new ArrayList<>();

        Set<Long> processedProductIds = new HashSet<>();

        for (ProductRecommendation recommendation :
                aiResponse.getRecommendations()) {

            if (recommendation == null ||
                    recommendation.getProductId() == null) {
                continue;
            }

            Long productId = recommendation.getProductId();

            // Only allow products from the eligible candidate list
            if (!eligibleProductIds.contains(productId)) {
                continue;
            }

            // Prevent duplicate recommendations
            if (!processedProductIds.add(productId)) {
                continue;
            }

            Product product = productRepository.findById(productId)
                    .orElse(null);

            // Recheck current product availability
            if (product == null ||
                    product.getStock() == null ||
                    product.getStock() <= 0 ||
                    product.getName() == null) {
                continue;
            }

            String productName =
                    product.getName().toLowerCase(Locale.ROOT);

            // Exclude products already purchased or in the cart
            if (excludedProductNames.contains(productName)) {
                continue;
            }

            validRecommendations.add(recommendation);

            // Return at most 3 recommendations
            if (validRecommendations.size() == 3) {
                break;
            }
        }

        return new RecommendationResponse(validRecommendations);
    }
}
