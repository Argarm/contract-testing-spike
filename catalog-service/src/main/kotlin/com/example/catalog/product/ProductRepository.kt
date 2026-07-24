package com.example.catalog.product

import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class ProductRepository {
    private val products = listOf(
        Product("p-100", "Mechanical Keyboard", BigDecimal("89.99"), true),
        Product("p-200", "USB-C Hub", BigDecimal("39.50"), false),
    ).associateBy(Product::id)

    fun findById(id: String): Product? = products[id]
}
