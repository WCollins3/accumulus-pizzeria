package com.accumulus.pizzeria.repository.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass

@Entity
@IdClass(SuggestedProductCompositeKey::class)
class SuggestedProduct (
    @Id
    var emailId: Long,

    @Id
    var productName: String
)