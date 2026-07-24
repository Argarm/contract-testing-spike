package com.example.checkout

import com.example.checkout.catalog.CatalogClient
import com.example.checkout.catalog.CatalogProduct
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.math.BigDecimal

class CheckoutServiceTest {
    private val catalogClient = mock(CatalogClient::class.java)
    private val checkoutService = CheckoutService(catalogClient)

    @Test
    fun `calculates total from catalog price and quantity`() {
        `when`(catalogClient.getProduct("p-100"))
            .thenReturn(CatalogProduct("p-100", "Mechanical Keyboard", BigDecimal("89.99"), true))

        val result = checkoutService.checkout(CheckoutRequest("p-100", 2))

        assertEquals(CheckoutResponse("p-100", 2, BigDecimal("179.98")), result)
    }
}