package com.shivani.smart_cart_engine.ai.controller;

import com.shivani.smart_cart_engine.ai.dto.ProductCandidate;
import com.shivani.smart_cart_engine.ai.service.ProductCandidateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class ProductCandidateController {

    private final ProductCandidateService productCandidateService;

    public ProductCandidateController(
            ProductCandidateService productCandidateService) {
        this.productCandidateService = productCandidateService;
    }

    @GetMapping("/candidates")
    public List<ProductCandidate> getCandidates() {
        return productCandidateService.getEligibleProducts();
    }
}