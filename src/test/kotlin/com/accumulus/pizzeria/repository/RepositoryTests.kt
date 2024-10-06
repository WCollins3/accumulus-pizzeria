package com.accumulus.pizzeria.repository

import com.accumulus.pizzeria.repository.model.Email
import com.accumulus.pizzeria.repository.model.SuggestedProduct
import com.accumulus.pizzeria.repository.model.Topping
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.EmptyResultDataAccessException
import java.util.LinkedList

@DataJpaTest
class RepositoryTests @Autowired constructor(
    val emailRepository: EmailRepository,
    val toppingRepository: ToppingRepository,
    val voteRepository: VoteRepository,
    val suggestedProductRepository: SuggestedProductRepository) {

    @Test
    fun `Test emailRepository findByEmail Returns correct values`() {
        val testEmail = Email(email = "test@test.com")
        emailRepository.save(testEmail)
        val found = emailRepository.findByEmail(testEmail.email)
        assertThat(found).isEqualTo(testEmail)
    }

    @Test
    fun `Test toppingRepository findAllByEmailId Returns correct values and toppingRepository deletaAllByEmailId deletes all`() {
        val testEmail = Email(email = "test@test.com")
        emailRepository.save(testEmail)
        val otherEmail = Email(email = "other@test.com")
        emailRepository.save(otherEmail)

        val testToppings: LinkedList<Topping> = LinkedList()
        testToppings.add(Topping(testEmail.id!!, "cheese"))
        testToppings.add(Topping(testEmail.id!!, "peperoni"))
        testToppings.add(Topping(testEmail.id!!, "sausage"))

        toppingRepository.saveAll(testToppings)

        val otherTopping: Topping = Topping(otherEmail.id!!, "yogurt")
        toppingRepository.save(otherTopping)

        val testResults = toppingRepository.findAllByEmailId(testEmail.id!!)
        assertThat(testResults).hasSize(3);
        for (foundTopping in testResults) {
            assertThat(foundTopping).isNotEqualTo(otherTopping)
        }

        toppingRepository.deleteAllByEmailId(testEmail.id!!)

        val remainingResults = toppingRepository.findAll();
        assertThat(remainingResults).hasSize(1)
        assertThat(remainingResults.elementAt(0).toppingName).isEqualTo(otherTopping.toppingName)
        assertThat(remainingResults.elementAt(0).emailId).isEqualTo(otherTopping.emailId)
    }

    @Test
    fun `Test voteRepository findAll returns all counts in expected order`() {
        val email1 = Email(email = "email1@test.com")
        val email2 = Email(email = "email2@test.com")
        val email3 = Email(email = "email3@test.com")

        emailRepository.save(email1)
        emailRepository.save(email2)
        emailRepository.save(email3)

        val toppings1: LinkedList<Topping> = LinkedList()
        toppings1.add(Topping(email1.id!!, "cheese"))
        toppings1.add(Topping(email1.id!!, "pepper"))
        toppings1.add(Topping(email1.id!!, "onion"))
        toppingRepository.saveAll(toppings1)

        val toppings2: LinkedList<Topping> = LinkedList()
        toppings2.add(Topping(email2.id!!, "cheese"))
        toppings2.add(Topping(email2.id!!, "pepper"))
        toppingRepository.saveAll(toppings2)

        val topping3 = Topping(email3.id!!, "cheese")
        toppingRepository.save(topping3)

        val toppingResults = toppingRepository.findAll()
        assertThat(toppingResults).hasSize(6)

        val results = voteRepository.findAll()

        assertThat(results).hasSize(3);
        assertThat(results.elementAt(0).toppingName).isEqualTo("cheese")
        assertThat(results.elementAt(0).votes).isEqualTo(3)
        assertThat(results.elementAt(1).toppingName).isEqualTo("pepper")
        assertThat(results.elementAt(1).votes).isEqualTo(2)
        assertThat(results.elementAt(2).toppingName).isEqualTo("onion")
        assertThat(results.elementAt(2).votes).isEqualTo(1)
    }

    @Test
    fun `Test suggestedProductRepository findAllByEmailId finds all products and deleteByEmailIdAndSuggestedProduct deletes correct values`() {
        val testEmail = Email(email = "test@test.com")
        emailRepository.save(testEmail)
        val otherEmail = Email(email = "other@test.com")
        emailRepository.save(otherEmail)

        val suggestedProduct1 = SuggestedProduct(testEmail.id!!, "zeppole")
        val suggestedProduct2 = SuggestedProduct(testEmail.id!!, "cheesesteak")
        val suggestedProduct3 = SuggestedProduct(testEmail.id!!, "cannoli")

        suggestedProductRepository.save(suggestedProduct1)
        suggestedProductRepository.save(suggestedProduct2)
        suggestedProductRepository.save(suggestedProduct3)

        val otherSuggestedProduct = SuggestedProduct(otherEmail.id!!, "justice")
        suggestedProductRepository.save(otherSuggestedProduct)

        val results = suggestedProductRepository.findAllByEmailId(testEmail.id!!)

        assertThat(results).hasSize(3)

        val singleResult = suggestedProductRepository.findByEmailIdAndProductName(testEmail.id!!, "cheesesteak")
        assertThat(singleResult.emailId).isEqualTo(suggestedProduct2.emailId)
        assertThat(singleResult.productName).isEqualTo(suggestedProduct2.productName)

        suggestedProductRepository.deleteByEmailIdAndProductName(testEmail.id!!, "cheesesteak")
        val resultsAfterDelete = suggestedProductRepository.findAllByEmailId(testEmail.id!!)

        assertThat(resultsAfterDelete).hasSize(2)

        for (product in resultsAfterDelete) {
            assertThat(product.productName).isNotEqualTo("cheesesteak")
        }

        try {
            val missingProduct = suggestedProductRepository.findByEmailIdAndProductName(testEmail.id!!, "cheesesteak")
            assertThat(true).isFalse() // force fail if this works
        } catch (ex: EmptyResultDataAccessException) {
            // successfully thrown
            assertThat(true).isTrue()
        }
    }
}