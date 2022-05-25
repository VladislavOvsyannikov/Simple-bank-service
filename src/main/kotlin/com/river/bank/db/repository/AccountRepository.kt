package com.river.bank.db.repository

import com.river.bank.db.entity.Account
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType

interface AccountRepository : JpaRepository<Account, Long> {

    fun existsByNumber(number: String): Boolean

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByNumber(number: String): Account?

    @EntityGraph(attributePaths = ["person"])
    override fun findAll(pageable: Pageable): Page<Account>

}
