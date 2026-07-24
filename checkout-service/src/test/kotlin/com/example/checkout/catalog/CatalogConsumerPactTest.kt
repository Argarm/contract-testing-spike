package com.example.checkout.catalog

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTest
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.V4Pact
import au.com.dius.pact.core.model.annotations.Pact
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@PactConsumerTest
class CatalogConsumerPactTest {

    @Pact(provider = "catalog-service", consumer = "checkout-service")
    fun productExists(builder: PactDslWithProvider): V4Pact =
        builder
            .given("product p-100 exists")
            .uponReceiving("a request for product p-100")
            .path("/products/p-100")
            .method("GET")
            .willRespondWith()
            .status(200)
            .headers(mapOf("Content-Type" to "application/json"))
            .body(productBody("p-100", "Mechanical Keyboard", 89.99, true))
            .toPact(V4Pact::class.java)

    @Pact(provider = "catalog-service", consumer = "checkout-service")
    fun unavailableProduct(builder: PactDslWithProvider): V4Pact =
        builder
            .given("product p-200 exists and is unavailable")
            .uponReceiving("a request for unavailable product p-200")
            .path("/products/p-200")
            .method("GET")
            .willRespondWith()
            .status(200)
            .headers(mapOf("Content-Type" to "application/json"))
            .body(productBody("p-200", "USB-C Hub", 39.50, false))
            .toPact(V4Pact::class.java)

    @Test
    @PactTestFor(providerName = "catalog-service", pactMethod = "productExists")
    fun `gets a product from catalog`(mockServer: MockServer) {
        val product = CatalogClient(RestClientFactory.create(mockServer.getUrl())).getProduct("p-100")

        assertAll(
            { assertEquals("p-100", product.id) },
            { assertEquals("Mechanical Keyboard", product.name) },
            { assertEquals(BigDecimal("89.99"), product.price) },
            { assertEquals(true, product.available) },
        )
    }

    @Test
    @PactTestFor(providerName = "catalog-service", pactMethod = "unavailableProduct")
    fun `gets an unavailable product from catalog`(mockServer: MockServer) {
        val product = CatalogClient(RestClientFactory.create(mockServer.getUrl())).getProduct("p-200")

        assertAll(
            { assertEquals("p-200", product.id) },
            { assertEquals("USB-C Hub", product.name) },
            { assertTrue(product.price.compareTo(BigDecimal("39.50")) == 0) },
            { assertEquals(false, product.available) },
        )
    }

    private fun productBody(id: String, name: String, price: Double, available: Boolean) =
        newJsonBody { body ->
            body.stringType("id", id)
            body.stringType("name", name)
            body.decimalType("price", price)
            body.booleanType("available", available)
        }.build()
}