package com.river.bank.support.validator

import com.river.bank.support.constant.BankConstants.AMOUNT_INTEGER_PART_LIMIT
import com.river.bank.support.constant.BankConstants.AMOUNT_SCALE_LIMIT
import java.math.BigDecimal
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [AmountValidator::class])
annotation class AmountValidation(
    val message: String = "Amount must be positive, scale must be not more than $AMOUNT_SCALE_LIMIT " +
            "and integer part must be not more than $AMOUNT_INTEGER_PART_LIMIT",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class AmountValidator : ConstraintValidator<AmountValidation, BigDecimal> {
    override fun isValid(value: BigDecimal, context: ConstraintValidatorContext): Boolean {
        if (value <= BigDecimal.ZERO) {
            return false
        }

        return value.stripTrailingZeros()
            .let { it.scale() <= AMOUNT_SCALE_LIMIT && it.precision() - it.scale() <= AMOUNT_INTEGER_PART_LIMIT }
    }
}
