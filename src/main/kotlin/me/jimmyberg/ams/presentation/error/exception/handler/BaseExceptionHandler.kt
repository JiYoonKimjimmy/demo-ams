package me.jimmyberg.ams.presentation.error.exception.handler

import me.jimmyberg.ams.common.error.ErrorCode
import me.jimmyberg.ams.common.error.ErrorResponse
import me.jimmyberg.ams.common.error.FeatureCode
import me.jimmyberg.ams.common.error.exception.BaseException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class BaseExceptionHandler(
    private val featureCode: FeatureCode = FeatureCode.UNKNOWN
) {
    // logger
    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponseEntity(featureCode, e.errorCode, e.detailMessage)
    }

    @ExceptionHandler(Exception::class)
    fun exceptionHandler(e: Exception): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponseEntity(featureCode, ErrorCode.UNKNOWN_ERROR, e.message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        logger.error("Method argument not valid: ${e.message}")
        val message = e.bindingResult.fieldErrors
            .takeIf { it.isNotEmpty() }
            ?.joinToString(separator = "; ") { fieldError ->
                val constraint = fieldError.code
                    ?: fieldError.codes?.firstOrNull()?.substringBefore('.')
                    ?: "Invalid"
                val phrase = when (constraint) {
                    "NotBlank", "NotNull", "NotEmpty" -> "is required"
                    "Positive" -> "must be positive"
                    "PositiveOrZero" -> "must be zero or positive"
                    "Email" -> "must be a well-formed email address"
                    else -> fieldError.defaultMessage ?: "is invalid"
                }
                "'${fieldError.field}' $phrase"
            }
            ?: "Validation failed"
        return ErrorResponse.toResponseEntity(featureCode, ErrorCode.MISSING_REQUIRED_DATA, message)
    }

}

