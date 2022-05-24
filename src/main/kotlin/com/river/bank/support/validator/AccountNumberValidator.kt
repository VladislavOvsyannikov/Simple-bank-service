package com.river.bank.support.validator

import com.river.bank.support.constant.BankConstants.ACCOUNT_NUMBER_LENGTH
import org.apache.commons.lang3.StringUtils
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [AccountNumberValidator::class])
annotation class AccountNumberValidation(
    val message: String = "Account number must be exactly $ACCOUNT_NUMBER_LENGTH-digits",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class AccountNumberValidator : ConstraintValidator<AccountNumberValidation, String> {
    override fun isValid(value: String, context: ConstraintValidatorContext) =
        value.length == ACCOUNT_NUMBER_LENGTH && StringUtils.isNumeric(value)
}
