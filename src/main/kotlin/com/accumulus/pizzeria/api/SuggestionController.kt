package com.accumulus.pizzeria.api

import com.accumulus.pizzeria.api.dto.PostSuggestionDto
import com.accumulus.pizzeria.repository.EmailRepository
import com.accumulus.pizzeria.repository.SuggestedProductRepository
import com.accumulus.pizzeria.repository.model.Email
import com.accumulus.pizzeria.repository.model.SuggestedProduct
import jakarta.transaction.Transactional
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/suggestion")
class SuggestionController(private val emailRepository: EmailRepository, private val suggestedProductRepository: SuggestedProductRepository) {

    @PostMapping("/product")
    @Transactional
    fun postProductSuggestion(@RequestBody suggestionDto: PostSuggestionDto): ResponseEntity<Unit> {
        // find the existing email or create it if it doesn't exist
        var email = emailRepository.findByEmail(suggestionDto.email)
        if (email == null) {
            val newEmail = Email(email = suggestionDto.email)
            emailRepository.save(newEmail)
            email = newEmail
        }

        try {
            // do nothing if suggestion already exists
            var existingSuggestion = suggestedProductRepository.findByEmailIdAndProductName(email.id!!, suggestionDto.suggestion)
        } catch (ex: EmptyResultDataAccessException) {
            suggestedProductRepository.save(SuggestedProduct(email.id!!, suggestionDto.suggestion))
        }

        return ResponseEntity(HttpStatus.ACCEPTED)
    }
}