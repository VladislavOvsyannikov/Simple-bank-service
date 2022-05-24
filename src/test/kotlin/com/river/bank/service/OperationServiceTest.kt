package com.river.bank.service

import com.river.bank.db.entity.Account
import com.river.bank.db.entity.Operation
import com.river.bank.db.entity.Person
import com.river.bank.db.entity.type.OperationType.DEPOSIT
import com.river.bank.db.repository.OperationRepository
import com.river.bank.dto.request.DepositRequestDto
import com.river.bank.dto.request.TransferRequestDto
import com.river.bank.dto.request.WithdrawRequestDto
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
internal class OperationServiceTest {

    @Mock
    lateinit var operationRepository: OperationRepository
    @Mock
    lateinit var accountService: AccountService
    @Mock
    lateinit var passwordEncoder: PasswordEncoder
    @InjectMocks
    lateinit var operationService: OperationService

    @Test
    fun find() {
        val account = Account("number", "pin", BigDecimal.ZERO, Person("login"))

        `when`(operationRepository.findBySourceIdOrDestId(anyLong(), anyLong(), any()))
            .thenReturn(PageImpl(listOf(Operation(DEPOSIT, BigDecimal.ONE, null, account))))

        val operations = operationService.find(100, Pageable.unpaged())

        Assertions.assertEquals(1, operations.size)
        Assertions.assertEquals(DEPOSIT, operations.content[0].type)
        verify(operationRepository, times(1)).findBySourceIdOrDestId(anyLong(), anyLong(), any())
    }

    @Test
    fun deposit() {
        `when`(accountService.findByNumber(anyString()))
            .thenReturn(Account("number", "pin", BigDecimal.ZERO, Person("login")))
        `when`(accountService.save(any())).thenAnswer { it.getArgument(0) }
        `when`(operationRepository.save(any())).thenAnswer { it.getArgument(0) }

        operationService.deposit(DepositRequestDto("number", BigDecimal.ONE))

        verify(accountService, times(1)).findByNumber(anyString())
        verify(accountService, times(1)).save(any())
        verify(operationRepository, times(1)).save(any())
    }

    @Test
    fun withdraw_insufficientFunds_exception() {
        `when`(accountService.findByNumber(anyString()))
            .thenReturn(Account("number", "pin", BigDecimal.ZERO, Person("login")))

        assertThrows<IllegalArgumentException> {
            operationService.withdraw(WithdrawRequestDto("number", "pin", BigDecimal.ONE))
        }

        verify(accountService, times(1)).findByNumber(anyString())
    }

    @Test
    fun withdraw_incorrectPinCode_exception() {
        `when`(accountService.findByNumber(anyString()))
            .thenReturn(Account("number", "pin", BigDecimal("10"), Person("login")))
        `when`(passwordEncoder.matches(anyString(), anyString())).thenReturn(false)

        assertThrows<IllegalArgumentException> {
            operationService.withdraw(WithdrawRequestDto("number", "pin", BigDecimal.ONE))
        }

        verify(accountService, times(1)).findByNumber(anyString())
        verify(passwordEncoder, times(1)).matches(anyString(), anyString())
    }

    @Test
    fun withdraw_sufficientFundsAndCorrectPinCode_success() {
        `when`(accountService.findByNumber(anyString()))
            .thenReturn(Account("number", "pin", BigDecimal("10"), Person("login")))
        `when`(passwordEncoder.matches(anyString(), anyString())).thenReturn(true)
        `when`(accountService.save(any())).thenAnswer { it.getArgument(0) }
        `when`(operationRepository.save(any())).thenAnswer { it.getArgument(0) }

        operationService.withdraw(WithdrawRequestDto("number", "pin", BigDecimal.ONE))

        verify(accountService, times(1)).findByNumber(anyString())
        verify(passwordEncoder, times(1)).matches(anyString(), anyString())
        verify(accountService, times(1)).save(any())
        verify(operationRepository, times(1)).save(any())
    }

    @Test
    fun transfer() {
        `when`(accountService.findByNumber(anyString()))
            .thenReturn(Account("number", "pin", BigDecimal("10"), Person("login")))
        `when`(passwordEncoder.matches(anyString(), anyString())).thenReturn(true)
        `when`(accountService.save(any())).thenAnswer { it.getArgument(0) }
        `when`(operationRepository.save(any())).thenAnswer { it.getArgument(0) }

        operationService.transfer(TransferRequestDto("number1", "pin", "number2", BigDecimal.ONE))

        verify(accountService, times(2)).findByNumber(anyString())
        verify(passwordEncoder, times(1)).matches(anyString(), anyString())
        verify(accountService, times(2)).save(any())
        verify(operationRepository, times(1)).save(any())
    }

}
