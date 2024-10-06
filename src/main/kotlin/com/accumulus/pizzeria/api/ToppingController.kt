package com.accumulus.pizzeria.api

import com.accumulus.pizzeria.api.dto.ToppingsDto
import com.accumulus.pizzeria.repository.EmailRepository
import com.accumulus.pizzeria.repository.ToppingRepository
import com.accumulus.pizzeria.repository.VoteRepository
import com.accumulus.pizzeria.repository.model.Email
import com.accumulus.pizzeria.repository.model.Topping
import com.accumulus.pizzeria.repository.model.Vote
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.LinkedList

@RestController
@RequestMapping("/topping")
class ToppingController(private val emailRepository: EmailRepository, private val toppingRepository: ToppingRepository, private val voteRepository: VoteRepository) {

    @PostMapping
    @Transactional
    fun postToppings(@RequestBody toppings: ToppingsDto): ResponseEntity<Unit> {
        var email = emailRepository.findByEmail(toppings.email)
        if (email == null) {
            val newEmail = Email(email = toppings.email)
            emailRepository.save(newEmail)
            email = newEmail
        } else {
            toppingRepository.deleteAllByEmailId(email.id!!)
        }

        val toppingsToSave: LinkedList<Topping> = LinkedList()
        for (topping in toppings.toppings) {
            toppingsToSave.add(Topping(email.id!!, topping))
        }

        toppingRepository.saveAll(toppingsToSave)

        return ResponseEntity(HttpStatus.ACCEPTED)
    }

    @GetMapping("/votes")
    fun getVotes(): List<Vote> {
        return voteRepository.findAll();
    }
}