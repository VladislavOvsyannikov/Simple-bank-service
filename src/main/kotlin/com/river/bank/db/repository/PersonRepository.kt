package com.river.bank.db.repository

import com.river.bank.db.entity.Person
import org.springframework.data.jpa.repository.JpaRepository

interface PersonRepository : JpaRepository<Person, Long> {
    fun existsByLoginIgnoreCase(login: String): Boolean
}
