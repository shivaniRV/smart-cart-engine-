package com.shivani.smart_cart_engine.order.service;

import com.shivani.smart_cart_engine.cart.entity.Cart;
import com.shivani.smart_cart_engine.cart.entity.CartItem;
import com.shivani.smart_cart_engine.cart.repository.CartRepository;
import com.shivani.smart_cart_engine.cart.repository.CartItemRepository;

import com.shivani.smart_cart_engine.order.dto.OrderItemResponse;
import com.shivani.smart_cart_engine.order.dto.OrderResponse;
import com.shivani.smart_cart_engine.order.entity.Order;
import com.shivani.smart_cart_engine.order.entity.OrderItem;
import com.shivani.smart_cart_engine.order.repository.OrderRepository;

import com.shivani.smart_cart_engine.product.entity.Product;
import com.shivani.smart_cart_engine.product.repository.ProductRepository;

import com.shivani.smart_cart_engine.user.entity.User;
import com.shivani.smart_cart_engine.user.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponse checkout(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cart not found"));

        List<CartItem> cartItems =
                cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PLACED");

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {

            Long productId = cartItem.getProduct().getId();
            Integer quantity = cartItem.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Product not found"));

            if (product.getPrice() == null ||
                    product.getName() == null ||
                    product.getStock() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Product information is incomplete");
            }

            int updatedRows =
                    productRepository.reduceStock(productId, quantity);

            if (updatedRows == 0) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Insufficient stock for product: "
                                + product.getName());
            }

            BigDecimal price = product.getPrice();

            BigDecimal subtotal =
                    price.multiply(BigDecimal.valueOf(quantity));

            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(product);
            orderItem.setProductName(product.getName());
            orderItem.setPrice(price);
            orderItem.setQuantity(quantity);
            orderItem.setSubtotal(subtotal);

            order.addItem(orderItem);

            totalAmount = totalAmount.add(subtotal);
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteByCartId(cart.getId());

        return buildOrderResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrderHistory(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found");
        }

        return orderRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::buildOrderResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long userId, Long orderId) {

        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Order not found"));

        return buildOrderResponse(order);
    }

    private OrderResponse buildOrderResponse(Order order) {

        List<OrderItemResponse> itemResponses =
                order.getItems().stream()
                        .map(this::buildOrderItemResponse)
                        .toList();

        OrderResponse response = new OrderResponse();

        response.setOrderId(order.getId());
        response.setUserId(order.getUser().getId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setItems(itemResponses);

        return response;
    }

    private OrderItemResponse buildOrderItemResponse(OrderItem item) {

        OrderItemResponse response = new OrderItemResponse();

        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProductName());
        response.setPrice(item.getPrice());
        response.setQuantity(item.getQuantity());
        response.setSubtotal(item.getSubtotal());

        return response;
    }
}