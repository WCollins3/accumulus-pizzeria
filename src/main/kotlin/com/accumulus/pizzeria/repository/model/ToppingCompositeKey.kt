package com.accumulus.pizzeria.repository.model

import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
class ToppingCompositeKey (
    var emailId: Long,
    var toppingName: String
): Serializable