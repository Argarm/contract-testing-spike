package com.example.catalog.product

import java.math.BigDecimal

data class Product(
    val id: String,
    val name: String,
    val price: BigDecimal,
    val available: Boolean,
)
