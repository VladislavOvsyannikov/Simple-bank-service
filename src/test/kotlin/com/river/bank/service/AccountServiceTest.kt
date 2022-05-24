package com.river.bank.service

import com.river.bank.db.entity.Account
import com.river.bank.db.entity.Person
import com.river.bank.db.repository.AccountRepository
import com.river.bank.dto.request.AccountRequestDto
import com.river.bank.support.constant.BankConstants.ACCOUNT_NUMBER_LENGTH
import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.anyString
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import java.math.BigDecimal
import javax.persistence.EntityNotFoundException

@ExtendWith(MockitoExtension::class)
internal class AccountServiceTest {

    @Mock
    lateinit var accountRepository: AccountRepository
    @Mock
    lateinit var personService: PersonService
    @Mock
    lateinit var passwordEncoder: PasswordEncoder
    @InjectMocks
    lateinit var accountService: AccountService

    @Test
    fun find() {
        `when`(accountRepository.findAll(any<Pageable>()))
            .thenReturn(PageImpl(listOf(Account("number", "pin", BigDecimal.ZERO, Person("login")))))

        val accounts = accountService.find(Pageable.unpaged())

        assertEquals(1, accounts.size)
        assertEquals("number", accounts.content[0].number)
        verify(accountRepository, times(1)).findAll(any<Pageable>())
    }

    @Test
    fun findByNumber_accountFound_success() {
        `when`(accountRepository.findByNumber(anyString()))
            .thenReturn(Account("number", "pin", BigDecimal.ZERO, Person("login")))

        val account = accountService.findByNumber("number")

        assertEquals("number", account.number)
        verify(accountRepository, times(1)).findByNumber(anyString())
    }

    @Test
    fun findByNumber_accountNotFound_exception() {
        `when`(accountRepository.findByNumber(anyString())).thenReturn(null)

        assertThrows<EntityNotFoundException> { accountService.findByNumber("number") }

        verify(accountRepository, times(1)).findByNumber(anyString())
    }

    @Test
    fun create() {
        `when`(personService.findById(anyLong())).thenReturn(Person("login"))
        `when`(accountRepository.existsByNumber(anyString())).thenReturn(false)
        `when`(passwordEncoder.encode(anyString())).thenReturn("encoded")
        `when`(accountRepository.save(any())).thenAnswer { it.getArgument(0) }

        val account = accountService.create(AccountRequestDto("pin", 100))

        assertEquals(ACCOUNT_NUMBER_LENGTH, account.number.length)
        assertTrue(StringUtils.isNumeric(account.number))
        verify(personService, times(1)).findById(anyLong())
        verify(accountRepository, times(1)).existsByNumber(anyString())
        verify(passwordEncoder, times(1)).encode(anyString())
        verify(accountRepository, times(1)).save(any())
    }

}
