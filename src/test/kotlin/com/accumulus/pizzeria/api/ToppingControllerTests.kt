package com.accumulus.pizzeria.api

import com.accumulus.pizzeria.repository.EmailRepository
import com.accumulus.pizzeria.repository.ToppingRepository
import com.accumulus.pizzeria.repository.VoteRepository
import com.accumulus.pizzeria.repository.model.Email
import com.accumulus.pizzeria.repository.model.Topping
import com.accumulus.pizzeria.repository.model.Vote
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ToppingController::class)
class ToppingControllerTests @Autowired constructor(
    private val mockMvc: MockMvc) {

    @MockBean
    private lateinit var emailRepository: EmailRepository

    @MockBean
    private lateinit var toppingRepository: ToppingRepository

    @MockBean
    private lateinit var voteRepository: VoteRepository

    @Test
    fun `Test post endpoint creates new email entry and adds votes`() {
        val email = "test@test.com"

        given(emailRepository.findByEmail(email)).willReturn(null)

        given(emailRepository.save(any(Email::class.java))).willAnswer {
            val emailToSave = it.arguments[0] as Email
            emailToSave.id = 1
            emailToSave
        }

        given(toppingRepository.saveAll(any<List<Topping>>())).willReturn(emptyList())

        mockMvc.perform(
            MockMvcRequestBuilders.post("/topping")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "$email", "toppings": ["pepper", "mushroom"]}""")
        )
            .andExpect(status().isAccepted)
    }

    @Test
    fun `Test post endpoint deletes existing votes if email has already voted`() {
        val email = "test@test.com"
        val existingEmail = Email(email = email, id = 1)

        given(emailRepository.findByEmail(email)).willReturn(existingEmail)
        given(toppingRepository.deleteAllByEmailId(existingEmail.id!!)).willAnswer { Unit }
        given(toppingRepository.saveAll(any<List<Topping>>())).willReturn(emptyList())

        mockMvc.perform(
            MockMvcRequestBuilders.post("/topping")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "$email", "toppings": ["pepper", "mushroom"]}""")
        )
            .andExpect(status().isAccepted)
    }

    @Test
    fun `Test votes endpoint returns vote counts`() {
        val voteList = listOf(Vote(toppingName = "pepper", votes = 5))
        given(voteRepository.findAll()).willReturn(voteList)

        mockMvc.perform(MockMvcRequestBuilders.get("/topping/votes"))
            .andExpect(status().isOk)
            .andExpect { result ->
                val content = result.response.contentAsString
                assertThat(content).contains("pepper")
                assertThat(content).contains("5")
            }
    }
}