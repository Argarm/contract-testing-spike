package com.example.checkout

import java.math.BigDecimal

data class CheckoutRequest(
    val productId: String,
    val quantity: Int,
)

data class CheckoutResponse(
    val productId: String,
    val quantity: Int,
    val total: BigDecimal,
)