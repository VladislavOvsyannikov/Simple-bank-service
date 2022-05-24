package com.river.bank.db.repository

import com.river.bank.db.entity.Account
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {

    fun existsByNumber(number: String): Boolean

    fun findByNumber(number: String): Account?

    @EntityGraph(attributePaths = ["person"])
    override fun findAll(pageable: Pageable): Page<Account>

}
