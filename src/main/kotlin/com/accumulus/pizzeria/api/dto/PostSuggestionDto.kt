package com.accumulus.pizzeria.api.dto

import jakarta.annotation.Nonnull

data class PostSuggestionDto (
    @Nonnull
    val email: String,

    @Nonnull
    val suggestion: String
)