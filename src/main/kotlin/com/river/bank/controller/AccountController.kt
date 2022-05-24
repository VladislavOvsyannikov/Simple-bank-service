package com.river.bank.controller

import com.river.bank.dto.request.AccountRequestDto
import com.river.bank.service.AccountService
import com.river.bank.support.swagger.PageableApi
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Account")
@RequestMapping("/accounts")
class AccountController(private val accountService: AccountService) {

    @GetMapping
    @PageableApi
    @Operation(summary = "Return accounts with pagination")
    fun find(@Parameter(hidden = true) pageable: Pageable) = accountService.find(pageable)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new account")
    fun create(@RequestBody @Validated request: AccountRequestDto) = accountService.create(request)

}
