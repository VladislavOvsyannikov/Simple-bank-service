package com.river.bank.db.entity

import com.river.bank.dto.PersonDto
import javax.persistence.Entity

@Entity
data class Person(
    val login: String
) : BaseEntity()

fun Person.toDto() = PersonDto(id, login)
