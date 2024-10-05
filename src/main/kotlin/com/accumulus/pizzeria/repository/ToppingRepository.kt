package com.accumulus.pizzeria.repository

import com.accumulus.pizzeria.repository.model.Topping
import com.accumulus.pizzeria.repository.model.ToppingCompositeKey
import org.springframework.data.repository.CrudRepository

interface ToppingRepository : CrudRepository<Topping, ToppingCompositeKey> {
    fun findAllByEmailId(emailId: Long): Iterable<Topping>
    fun deleteAllByEmailId(emailId: Long)
}