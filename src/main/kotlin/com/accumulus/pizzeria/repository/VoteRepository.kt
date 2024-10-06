package com.accumulus.pizzeria.repository

import com.accumulus.pizzeria.repository.model.Vote
import org.springframework.data.jpa.repository.JpaRepository

interface VoteRepository : JpaRepository<Vote, Long> {
}