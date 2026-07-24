package com.example.checkout.catalog

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CatalogClientConfiguration(
    @Value("\${catalog.base-url}")
    private val catalogBaseUrl: String,
) {
    @Bean
    fun catalogClient(): CatalogClient =
        RestClientFactory.create(catalogBaseUrl).let(::CatalogClient)
}