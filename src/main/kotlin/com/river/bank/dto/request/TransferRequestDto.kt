package com.river.bank.dto.request

import com.river.bank.support.validator.AccountNumberValidation
import com.river.bank.support.validator.AmountValidation
import com.river.bank.support.validator.PinCodeValidation
import java.math.BigDecimal

class TransferRequestDto(

    @field:AccountNumberValidation
    val sourceNumber: String,

    @field:PinCodeValidation
    val pinCode: String,

    @field:AccountNumberValidation
    val destNumber: String,

    @field:AmountValidation
    val amount: BigDecimal

)
