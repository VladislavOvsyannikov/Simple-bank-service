package com.river.bank.controller

import com.river.bank.dto.request.DepositRequestDto
import com.river.bank.dto.request.TransferRequestDto
import com.river.bank.dto.request.WithdrawRequestDto
import com.river.bank.service.OperationService
import com.river.bank.support.swagger.PageableApi
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Operation")
@RequestMapping("/operations")
class OperationController(private val operationService: OperationService) {

    @GetMapping
    @PageableApi
    @Operation(summary = "Return account operations with pagination, default order by operation date descending")
    fun find(
        @Parameter(description = "Id of person account") @RequestParam accountId: Long,
        @Parameter(hidden = true) pageable: Pageable
    ) = operationService.find(accountId, pageable)

    @PutMapping("/deposit")
    @Operation(summary = "Deposit money into account")
    fun deposit(@RequestBody @Validated request: DepositRequestDto) = operationService.deposit(request)

    @PutMapping("/withdraw")
    @Operation(summary = "Withdraw money from account")
    fun withdraw(@RequestBody @Validated request: WithdrawRequestDto) = operationService.withdraw(request)

    @PutMapping("/transfer")
    @Operation(summary = "Transfer money from one account to another")
    fun transfer(@RequestBody @Validated request: TransferRequestDto) = operationService.transfer(request)

}
