package com.accumulus.pizzeria.repository

import com.accumulus.pizzeria.repository.model.Email
import org.springframework.data.repository.CrudRepository

interface EmailRepository : CrudRepository<Email, Long> {
    fun findByEmail(email: String): Email?
}