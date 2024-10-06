package com.accumulus.pizzeria.api.dto

import jakarta.annotation.Nonnull

data class ToppingsDto (
    @Nonnull
    val email: String,

    @Nonnull
    val toppings: List<String>
)