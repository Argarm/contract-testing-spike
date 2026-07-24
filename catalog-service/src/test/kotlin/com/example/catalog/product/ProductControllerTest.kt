package com.example.catalog.product

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class ProductControllerTest {
    private val mockMvc: MockMvc = MockMvcBuilders
        .standaloneSetup(ProductController(ProductRepository()))
        .build()

    @Test
    fun `returns product details`() {
        mockMvc.perform(get("/products/p-100"))
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith("application/json"))
            .andExpect(jsonPath("$.id").value("p-100"))
            .andExpect(jsonPath("$.name").value("Mechanical Keyboard"))
            .andExpect(jsonPath("$.price").value(89.99))
            .andExpect(jsonPath("$.available").value(true))
    }

    @Test
    fun `returns not found for unknown product`() {
        val result = mockMvc.perform(get("/products/unknown"))
            .andExpect(status().isNotFound)
            .andReturn()

        assertEquals(404, result.response.status)
    }
}
