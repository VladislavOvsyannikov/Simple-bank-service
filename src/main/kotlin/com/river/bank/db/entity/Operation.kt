package com.river.bank.db.entity

import com.river.bank.db.entity.type.OperationType
import com.river.bank.dto.OperationDto
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class Operation(

    @Enumerated(EnumType.STRING)
    val type: OperationType,

    val amount: BigDecimal,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    val source: Account?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dest_id")
    val dest: Account?

) : BaseEntity()

fun Operation.toDto() = OperationDto(id, createdAt, type, amount, source?.toDto(), dest?.toDto())
