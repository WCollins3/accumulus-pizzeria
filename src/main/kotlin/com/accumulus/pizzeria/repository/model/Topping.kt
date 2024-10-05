package com.accumulus.pizzeria.repository.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass

@Entity
@IdClass(ToppingCompositeKey::class)
class Topping(
    @Id
    var emailId: Long,

    @Id
    var topping: String
)
