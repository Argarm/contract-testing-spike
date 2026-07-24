package com.example.catalog.product

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ProductRepositoryTest {
    private val repository = ProductRepository()

    @Test
    fun `returns a product by id`() {
        assertEquals(
            Product("p-100", "Mechanical Keyboard", BigDecimal("89.99"), true),
            repository.findById("p-100"),
        )
    }

    @Test
    fun `returns null when product does not exist`() {
        assertNull(repository.findById("unknown"))
    }
}
