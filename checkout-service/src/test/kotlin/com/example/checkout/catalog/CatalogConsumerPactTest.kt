package com.example.checkout.catalog

import au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTest
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.consumer.junit5.PactTestForProvider
import au.com.dius.pact.core.model.V4Pact
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@PactConsumerTest
@PactTestForProvider("catalog-service")
class CatalogConsumerPactTest {

    @au.com.dius.pact.consumer.junit5.Pact(provider = "catalog-service", consumer = "checkout-service")
    fun productExists(builder: PactDslWithProvider): V4Pact =
        builder
            .given("product p-100 exists")
            .uponReceiving("a request for product p-100")
            .path("/products/p-100")
            .method("GET")
            .willRespondWith()
            .status(200)
            .header("Content-Type", "application/json")
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
    @PactTestFor(pactMethod = "productExists")
    fun `gets a product from catalog`() {
        // Deliberately red until CatalogClient is introduced in the next TDD step.
        assertEquals(true, false, "RED: CatalogClient is not implemented yet")
    }
}
