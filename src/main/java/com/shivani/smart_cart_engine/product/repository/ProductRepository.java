package com.shivani.smart_cart_engine.product.repository;

import com.shivani.smart_cart_engine.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Modifying(flushAutomatically = true)
    @Query("""
    UPDATE Product p
    SET p.stock = p.stock - :quantity
    WHERE p.id = :productId
    AND p.stock >= :quantity
""")    int reduceStock(
            @Param("productId") Long productId,
            @Param("quantity") Integer quantity
    );
    List<Product> findTop20ByStockGreaterThanOrderByIdAsc(Integer stock);
}