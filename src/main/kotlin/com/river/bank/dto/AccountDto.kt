package com.river.bank.dto

import java.math.BigDecimal

class AccountDto(
    val id: Long,
    val number: String,
    val balance: BigDecimal,
    val person: PersonDto
)
