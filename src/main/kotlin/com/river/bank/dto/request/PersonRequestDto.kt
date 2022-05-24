package com.river.bank.dto.request

import javax.validation.constraints.NotEmpty

class PersonRequestDto(

    @field:NotEmpty
    val login: String

)
