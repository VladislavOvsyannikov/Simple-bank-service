package com.river.bank.service

import com.river.bank.db.entity.Account
import com.river.bank.db.entity.Operation
import com.river.bank.db.entity.toDto
import com.river.bank.db.entity.type.OperationType
import com.river.bank.db.repository.OperationRepository
import com.river.bank.dto.request.DepositRequestDto
import com.river.bank.dto.request.TransferRequestDto
import com.river.bank.dto.request.WithdrawRequestDto
import mu.KotlinLogging
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class OperationService(
    private val operationRepository: OperationRepository,
    private val accountService: AccountService,
    private val passwordEncoder: PasswordEncoder
) {

    private val log = KotlinLogging.logger {}

    fun find(accountId: Long, pageable: Pageable) = operationRepository
        .findBySourceIdOrDestId(accountId, accountId, pageable)
        .map { it.toDto() }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun deposit(request: DepositRequestDto) {
        val dest = accountService.findByNumber(request.destNumber)

        dest.balance += request.amount
        accountService.save(dest)

        logOperation(OperationType.DEPOSIT, request.amount, dest = dest)
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun withdraw(request: WithdrawRequestDto) {
        val source = accountService.findByNumber(request.sourceNumber)
        checkSourceAccount(source, request.amount, request.pinCode)

        source.balance -= request.amount
        accountService.save(source)

        logOperation(OperationType.WITHDRAW, request.amount, source = source)
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun transfer(request: TransferRequestDto) {
        val source = accountService.findByNumber(request.sourceNumber)
        checkSourceAccount(source, request.amount, request.pinCode)
        val dest = accountService.findByNumber(request.destNumber)

        source.balance -= request.amount
        accountService.save(source)

        dest.balance += request.amount
        accountService.save(dest)

        logOperation(OperationType.TRANSFER, request.amount, source = source, dest = dest)
    }

    private fun checkSourceAccount(source: Account, amount: BigDecimal, pinCode: String) {
        require(source.balance >= amount) {
            "Insufficient funds"
        }

        require(passwordEncoder.matches(pinCode, source.pinCode)) {
            "Incorrect PIN code"
        }
    }

    private fun logOperation(
        type: OperationType,
        amount: BigDecimal,
        source: Account? = null,
        dest: Account? = null
    ) {
        val operation = Operation(type, amount, source, dest)
        operationRepository.save(operation)

        log.info { "[Success] [type: $type] [amount: $amount] [source: ${source?.number}] [dest: ${dest?.number}]" }
    }

}
