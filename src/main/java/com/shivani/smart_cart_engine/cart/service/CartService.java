package com.shivani.smart_cart_engine.cart.service;

import com.shivani.smart_cart_engine.cart.dto.AddToCartRequest;
import com.shivani.smart_cart_engine.cart.dto.CartItemResponse;
import com.shivani.smart_cart_engine.cart.dto.CartResponse;
import com.shivani.smart_cart_engine.cart.entity.Cart;
import com.shivani.smart_cart_engine.cart.entity.CartItem;
import com.shivani.smart_cart_engine.cart.repository.CartItemRepository;
import com.shivani.smart_cart_engine.cart.repository.CartRepository;
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
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository,
            ProductRepository productRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // Get an existing cart or create a new cart for the user
    public CartResponse getCart(Long userId) {

        Cart cart = getOrCreateCart(userId);

        return buildCartResponse(cart);
    }

    // Add a product to the cart
    public CartResponse addToCart(Long userId, AddToCartRequest request) {

        Cart cart = getOrCreateCart(userId);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found"));

        Integer requestedQuantity = request.getQuantity();

        // Check whether the product already exists in the cart
        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);

        int finalQuantity = requestedQuantity;

        if (cartItem != null) {
            finalQuantity = cartItem.getQuantity() + requestedQuantity;
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
        }

        // Check available stock
        if (finalQuantity > product.getStock()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Insufficient stock available");
        }

        cartItem.setQuantity(finalQuantity);

        cartItemRepository.save(cartItem);

        return buildCartResponse(cart);
    }

    // Update the quantity of an existing cart item
    public CartResponse updateCartItem(Long userId, AddToCartRequest request) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cart not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found"));

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found in cart"));

        Integer quantity = request.getQuantity();

        if (quantity > product.getStock()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Insufficient stock available");
        }

        cartItem.setQuantity(quantity);

        cartItemRepository.save(cartItem);

        return buildCartResponse(cart);
    }

    // Remove a product from the cart
    public CartResponse removeCartItem(Long userId, Long productId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cart not found"));

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found in cart"));

        cartItemRepository.delete(cartItem);

        return buildCartResponse(cart);
    }

    // Remove all products from the cart
    public CartResponse clearCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cart not found"));

        cartItemRepository.deleteByCartId(cart.getId());

        return buildCartResponse(cart);
    }

    // Find the user's cart or create one
    private Cart getOrCreateCart(Long userId) {

        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {

                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "User not found"));

                    Cart cart = new Cart();
                    cart.setUser(user);

                    return cartRepository.save(cart);
                });
    }

    // Convert Cart entity into CartResponse DTO
    private CartResponse buildCartResponse(Cart cart) {

        List<CartItem> cartItems =
                cartItemRepository.findByCartId(cart.getId());

        List<CartItemResponse> itemResponses = cartItems.stream()
                .map(this::buildCartItemResponse)
                .toList();

        BigDecimal totalAmount = itemResponses.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CartResponse response = new CartResponse();

        response.setCartId(cart.getId());
        response.setUserId(cart.getUser().getId());
        response.setItems(itemResponses);
        response.setTotalAmount(totalAmount);

        return response;
    }

    // Convert CartItem entity into CartItemResponse DTO
    private CartItemResponse buildCartItemResponse(CartItem cartItem) {

        Product product = cartItem.getProduct();

        BigDecimal price = product.getPrice();

        BigDecimal subtotal = price.multiply(
                BigDecimal.valueOf(cartItem.getQuantity()));

        CartItemResponse response = new CartItemResponse();

        response.setCartItemId(cartItem.getId());
        response.setProductId(product.getId());
        response.setProductName(product.getName());
        response.setQuantity(cartItem.getQuantity());
        response.setPrice(price);
        response.setSubtotal(subtotal);

        return response;
    }
}