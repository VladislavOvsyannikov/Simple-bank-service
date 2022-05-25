package com.river.bank

import com.river.bank.dto.request.AccountRequestDto
import com.river.bank.dto.request.DepositRequestDto
import com.river.bank.dto.request.PersonRequestDto
import com.river.bank.dto.request.TransferRequestDto
import com.river.bank.dto.request.WithdrawRequestDto
import com.river.bank.service.AccountService
import com.river.bank.service.OperationService
import com.river.bank.service.PersonService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import java.math.BigDecimal

@SpringBootTest
internal class BankApplicationIT {

    @Autowired
    lateinit var personService: PersonService
    @Autowired
    lateinit var accountService: AccountService
    @Autowired
    lateinit var operationService: OperationService

    @Test
    fun testBank() {
        // create person
        val person = personService.create(PersonRequestDto("login"))

        // create two bank accounts for that person
        val account1 = accountService.create(AccountRequestDto("1111", person.id))
        val account2 = accountService.create(AccountRequestDto("2222", person.id))

        // deposit money to each account
        operationService.deposit(DepositRequestDto(account1.number, BigDecimal(1_000)))
        operationService.deposit(DepositRequestDto(account2.number, BigDecimal(2_000)))

        // transfer all money from the first account to second in two transactions
        val transferRequest = TransferRequestDto(account1.number, "1111", account2.number, BigDecimal(500))
        operationService.transfer(transferRequest)
        operationService.transfer(transferRequest)
        assertThrows<IllegalArgumentException> { operationService.transfer(transferRequest) }

        // withdraw some money from the second account in two transactions
        val withdrawRequest = WithdrawRequestDto(account2.number, "2222", BigDecimal(1_000))
        operationService.withdraw(withdrawRequest)
        operationService.withdraw(withdrawRequest)

        // get accounts balances
        val balance1 = accountService.findByNumber(account1.number).balance
        val balance2 = accountService.findByNumber(account2.number).balance

        // check accounts balances
        assertEquals(0, BigDecimal.ZERO.compareTo(balance1))
        assertEquals(0, BigDecimal(1_000).compareTo(balance2))

        // get operations size for each account
        val account1OperationsSize = operationService.find(account1.id, Pageable.unpaged()).size
        val account2OperationsSize = operationService.find(account2.id, Pageable.unpaged()).size

        // check operations size
        assertEquals(3, account1OperationsSize)
        assertEquals(5, account2OperationsSize)
    }

}