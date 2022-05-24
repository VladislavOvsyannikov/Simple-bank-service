package com.river.bank.db.entity

import com.river.bank.dto.AccountDto
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class Account(

    val number: String,
    val pinCode: String,
    var balance: BigDecimal,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    val person: Person

) : BaseEntity()

fun Account.toDto() = AccountDto(id, number, balance, person.toDto())
