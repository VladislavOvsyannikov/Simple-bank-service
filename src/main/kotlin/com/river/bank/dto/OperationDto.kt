package com.river.bank.dto

import com.river.bank.db.entity.type.OperationType
import java.math.BigDecimal
import java.time.LocalDateTime

class OperationDto(
    val id: Long,
    val createdAt: LocalDateTime,
    val type: OperationType,
    val amount: BigDecimal,
    val source: AccountDto?,
    val dest: AccountDto?
)
