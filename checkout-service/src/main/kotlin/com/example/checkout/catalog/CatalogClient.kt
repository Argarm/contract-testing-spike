package com.example.checkout.catalog

import org.springframework.web.client.RestClient

class CatalogClient(
    private val restClient: RestClient,
) {
    fun getProduct(id: String): CatalogProduct =
        restClient.get()
            .uri("/products/{id}", id)
            .retrieve()
            .body(CatalogProduct::class.java)
            ?: error("Catalog returned an empty response for product $id")
}

object RestClientFactory {
    fun create(baseUrl: String): RestClient =
        RestClient.builder()
            .baseUrl(baseUrl)
            .build()
}
