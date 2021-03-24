package br.com.zup.pix.validator

import br.com.zup.pix.controller.request.CreatePixKeyRequest
import br.com.zup.pix.controller.shared.KeyType
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import io.micronaut.validation.validator.constraints.EmailValidator
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PixKeyValidator::class])
annotation class ValidPixKey(
    val message: String = "The pix key doesn't have a valid type and value.",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
class PixKeyValidator : ConstraintValidator<ValidPixKey, CreatePixKeyRequest> {

    override fun isValid(
        createKey: CreatePixKeyRequest?,
        annotationMetadata: AnnotationValue<ValidPixKey>,
        context: ConstraintValidatorContext
    ): Boolean {
        if (createKey == null || (createKey.keyValue == null || createKey.keyType == null)) {
            return true
        }

        if(createKey.keyValue.isBlank() && createKey.keyType != KeyType.RANDOM) {
            return false
        }

        val value = createKey.keyValue
        val type = createKey.keyType

        if (type == KeyType.CPF) {
            return validateCpf(value)
        }

        if (type == KeyType.EMAIL) {
            return validateEmail(value)
        }

        if (type == KeyType.TELL_NUMBER) {
            return validateTellNumber(value)
        }

        return false
    }

    private fun validateCpf(value: String): Boolean {
        return value.matches("^[0-9]{11}\$".toRegex())
    }

    private fun validateEmail(value: String): Boolean {
        return EmailValidator().run {
            initialize(null)
            isValid(value, null)
        }
    }

    private fun validateTellNumber(value: String): Boolean {
        return value.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
    }
}
