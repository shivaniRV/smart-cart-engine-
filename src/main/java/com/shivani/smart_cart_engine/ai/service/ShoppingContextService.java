package com.shivani.smart_cart_engine.ai.service;

import com.shivani.smart_cart_engine.ai.dto.ProductCandidate;
import com.shivani.smart_cart_engine.ai.dto.ShoppingContext;
import com.shivani.smart_cart_engine.cart.entity.Cart;
import com.shivani.smart_cart_engine.cart.entity.CartItem;
import com.shivani.smart_cart_engine.cart.repository.CartItemRepository;
import com.shivani.smart_cart_engine.cart.repository.CartRepository;
import com.shivani.smart_cart_engine.order.entity.Order;
import com.shivani.smart_cart_engine.order.entity.OrderItem;
import com.shivani.smart_cart_engine.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ShoppingContextService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final ProductCandidateService productCandidateService;

    public ShoppingContextService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            OrderRepository orderRepository,
            ProductCandidateService productCandidateService) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.productCandidateService = productCandidateService;
    }
    @Transactional(readOnly = true)
    public ShoppingContext buildShoppingContext(Long userId) {

        //
        // 1. Fetch current cart products
        List<String> cartProducts = cartRepository.findByUserId(userId)
                .map(Cart::getId)
                .map(cartItemRepository::findByCartId)
                .orElse(List.of())
                .stream()
                .map(CartItem::getProduct)
                .map(product -> product.getName())
                .toList();

        // 2. Fetch previously purchased products
        List<String> purchasedProducts = orderRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .flatMap(order -> order.getItems().stream())
                .map(OrderItem::getProductName)
                .toList();

        // 3. Fetch available products
        List<ProductCandidate> availableProducts =
                productCandidateService.getEligibleProducts();

        // 4. Combine everything
        return new ShoppingContext(
                userId,
                cartProducts,
                purchasedProducts,
                availableProducts
        );
    }
}