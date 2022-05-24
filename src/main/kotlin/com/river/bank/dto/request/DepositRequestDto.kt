package com.river.bank.dto.request

import com.river.bank.support.validator.AccountNumberValidation
import com.river.bank.support.validator.AmountValidation
import java.math.BigDecimal

class DepositRequestDto(

    @field:AccountNumberValidation
    val destNumber: String,

    @field:AmountValidation
    val amount: BigDecimal

)
