package com.river.bank.dto.request

import com.river.bank.support.validator.PinCodeValidation

class AccountRequestDto(

    @field:PinCodeValidation
    val pinCode: String,

    val personId: Long

)
