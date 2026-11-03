package com.shivani.smart_cart_engine.product.repository;

import com.shivani.smart_cart_engine.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}