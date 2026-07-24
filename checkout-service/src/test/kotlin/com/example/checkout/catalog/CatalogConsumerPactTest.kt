package com.example.checkout.catalog

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.core.model.annotations.Pact
import au.com.dius.pact.consumer.junit5.PactConsumerTest
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.V4Pact
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
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
            .body(
                newJsonBody { body ->
                    body.stringType("id", "p-100")
                    body.stringType("name", "Mechanical Keyboard")
                    body.decimalType("price", 89.99)
                    body.booleanType("available", true)
                }.build(),
            )
            .toPact(V4Pact::class.java)

    @Test
    @PactTestFor(providerName = "catalog-service", pactMethod = "productExists")
    fun `gets a product from catalog`(mockServer: MockServer) {
        val client = CatalogClient(RestClientFactory.create(mockServer.getUrl()))

        val product = client.getProduct("p-100")

        assertAll(
            { assertEquals("p-100", product.id) },
            { assertEquals("Mechanical Keyboard", product.name) },
            { assertEquals(BigDecimal("89.99"), product.price) },
            { assertEquals(true, product.available) },
        )
    }
}
