package com.example.checkout.catalog

import java.math.BigDecimal

data class CatalogProduct(
    val id: String,
    val name: String,
    val price: BigDecimal,
    val available: Boolean,
)
