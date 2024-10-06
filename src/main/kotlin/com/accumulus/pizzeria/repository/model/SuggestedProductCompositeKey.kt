package com.accumulus.pizzeria.repository.model

import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
class SuggestedProductCompositeKey (
    var emailId: Long,
    var productName: String
): Serializable