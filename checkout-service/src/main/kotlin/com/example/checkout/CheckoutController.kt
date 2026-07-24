package com.example.checkout

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/checkout")
class CheckoutController(
    private val checkoutService: CheckoutService,
) {
    @PostMapping
    fun checkout(@RequestBody request: CheckoutRequest): ResponseEntity<CheckoutResponse> =
        ResponseEntity.ok(checkoutService.checkout(request))
}