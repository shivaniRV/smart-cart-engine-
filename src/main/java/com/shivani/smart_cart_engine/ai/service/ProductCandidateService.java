package com.shivani.smart_cart_engine.ai.service;

import com.shivani.smart_cart_engine.ai.dto.ProductCandidate;
import com.shivani.smart_cart_engine.product.entity.Product;
import com.shivani.smart_cart_engine.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCandidateService {

    private final ProductRepository productRepository;

    public ProductCandidateService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductCandidate> getEligibleProducts() {

        List<Product> products =
                productRepository.findTop20ByStockGreaterThanOrderByIdAsc(0);

        return products.stream()
                .map(product -> new ProductCandidate(
                        product.getId(),
                        product.getName(),
                        product.getCategory(),
                        product.getDescription()
                ))
                .toList();
    }
}