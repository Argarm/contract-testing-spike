package com.example.checkout

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.math.BigDecimal

class CheckoutControllerTest {
    private val checkoutService = mock(CheckoutService::class.java)
    private val mockMvc: MockMvc = MockMvcBuilders
        .standaloneSetup(CheckoutController(checkoutService))
        .build()

    @Test
    fun `checks out a product`() {
        `when`(checkoutService.checkout(CheckoutRequest("p-100", 2)))
            .thenReturn(CheckoutResponse("p-100", 2, BigDecimal("179.98")))

        mockMvc.perform(
            post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"productId":"p-100","quantity":2}"""),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.productId").value("p-100"))
            .andExpect(jsonPath("$.quantity").value(2))
            .andExpect(jsonPath("$.total").value(179.98))
    }
}