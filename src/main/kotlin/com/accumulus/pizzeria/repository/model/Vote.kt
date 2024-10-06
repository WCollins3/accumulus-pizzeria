package com.accumulus.pizzeria.repository.model

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Vote (
    @Id
    val toppingName: String,
    val votes: Long
)