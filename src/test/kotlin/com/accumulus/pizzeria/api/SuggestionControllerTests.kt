package com.accumulus.pizzeria.api

import com.accumulus.pizzeria.repository.EmailRepository
import com.accumulus.pizzeria.repository.SuggestedProductRepository
import com.accumulus.pizzeria.repository.model.Email
import com.accumulus.pizzeria.repository.model.SuggestedProduct
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@WebMvcTest(SuggestionController::class)
class SuggestionControllerTests @Autowired constructor(
    private val mockMvc: MockMvc) {

    @MockBean
    private lateinit var emailRepository: EmailRepository

    @MockBean
    private lateinit var suggestedProductRepository: SuggestedProductRepository

    @Test
    fun `Test post endpoint creates new email and adds suggestion`() {
        val email = "test@test.com"
        val productName = "ice cream"

        given(emailRepository.findByEmail(email)).willReturn(null)

        given(emailRepository.save(any(Email::class.java))).willAnswer {
            val emailToSave = it.arguments[0] as Email
            emailToSave.id = 1
            emailToSave
        }

        given(suggestedProductRepository.findByEmailIdAndProductName(1, productName))
            .willThrow(EmptyResultDataAccessException(1))

        given(suggestedProductRepository.save(any<SuggestedProduct>())).willReturn(SuggestedProduct(1, productName))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/suggestion/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "$email", "suggestedProduct": "$productName"}""")
        )
    }

    @Test
    fun `Test post endpoint recognizes existing email and existing suggestion`() {
        val email = "test@test.com"
        val productName = "ice cream"

        given(emailRepository.findByEmail(email)).willReturn(Email(email, 1))

        given(suggestedProductRepository.findByEmailIdAndProductName(1, productName))
            .willReturn(SuggestedProduct(1, productName))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/suggestion/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "$email", "suggestedProduct": "$productName"}""")
        )
    }
}