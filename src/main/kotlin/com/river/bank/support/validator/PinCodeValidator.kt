package com.river.bank.support.validator

import com.river.bank.support.constant.BankConstants.PIN_CODE_LENGTH
import org.apache.commons.lang3.StringUtils
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [PinCodeValidator::class])
annotation class PinCodeValidation(
    val message: String = "PIN code must be exactly $PIN_CODE_LENGTH-digits",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class PinCodeValidator : ConstraintValidator<PinCodeValidation, String> {
    override fun isValid(value: String, context: ConstraintValidatorContext) =
        value.length == PIN_CODE_LENGTH && StringUtils.isNumeric(value)
}
