package com.accumulus.pizzeria.repository.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Email (
    var email: String,

    @Id
    @GeneratedValue
    var id: Long? = null
)