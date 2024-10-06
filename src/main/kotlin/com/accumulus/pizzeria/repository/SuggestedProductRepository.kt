package com.accumulus.pizzeria.repository

import com.accumulus.pizzeria.repository.model.SuggestedProduct
import com.accumulus.pizzeria.repository.model.SuggestedProductCompositeKey
import org.springframework.data.repository.CrudRepository

interface SuggestedProductRepository : CrudRepository<SuggestedProduct, SuggestedProductCompositeKey> {
    fun findAllByEmailId(emailId: Long): Iterable<SuggestedProduct>
    fun findByEmailIdAndProductName(emailId: Long, productName: String): SuggestedProduct
    fun deleteByEmailIdAndProductName(emailId: Long, productName: String)
}