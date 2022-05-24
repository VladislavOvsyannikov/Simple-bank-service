package com.river.bank.service

import com.river.bank.db.entity.Account
import com.river.bank.db.entity.toDto
import com.river.bank.db.repository.AccountRepository
import com.river.bank.dto.AccountDto
import com.river.bank.dto.request.AccountRequestDto
import com.river.bank.support.constant.BankConstants.ACCOUNT_NUMBER_LENGTH
import mu.KotlinLogging
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.persistence.EntityNotFoundException

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val personService: PersonService,
    private val passwordEncoder: PasswordEncoder
) {

    private val log = KotlinLogging.logger {}

    fun find(pageable: Pageable) = accountRepository
        .findAll(pageable)
        .map { it.toDto() }

    fun findByNumber(number: String) = accountRepository.findByNumber(number)
        ?: throw EntityNotFoundException("Account with number = $number was not found")

    fun create(request: AccountRequestDto): AccountDto {
        val person = personService.findById(request.personId)
        val newNumber = generateNumber()
        val encodedPinCode = passwordEncoder.encode(request.pinCode)

        val newAccount = Account(newNumber, encodedPinCode, BigDecimal.ZERO, person)
        val savedAccount = save(newAccount)

        log.info { "[Account created] [number: $newNumber]" }

        return savedAccount.toDto()
    }

    fun save(account: Account) = accountRepository.save(account)

    private fun generateNumber(): String {
        var number: String

        do {
            number = RandomStringUtils.randomNumeric(ACCOUNT_NUMBER_LENGTH)
        } while (accountRepository.existsByNumber(number))

        return number
    }

}
