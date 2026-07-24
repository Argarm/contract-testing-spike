package com.example.checkout

import com.example.checkout.catalog.CatalogClient
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class CheckoutService(
    private val catalogClient: CatalogClient,
) {
    fun checkout(request: CheckoutRequest): CheckoutResponse {
        require(request.quantity > 0) { "quantity must be greater than zero" }

        val product = catalogClient.getProduct(request.productId)
        require(product.available) { "product ${product.id} is not available" }

        return CheckoutResponse(
            productId = product.id,
            quantity = request.quantity,
            total = product.price.multiply(BigDecimal.valueOf(request.quantity.toLong())),
        )
    }
}