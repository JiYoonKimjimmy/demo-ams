package me.jimmyberg.ams.infrastructure.error.exception.handler

import me.jimmyberg.ams.infrastructure.error.ErrorCode
import me.jimmyberg.ams.infrastructure.error.ErrorResponse
import me.jimmyberg.ams.infrastructure.error.FeatureCode
import me.jimmyberg.ams.infrastructure.error.exception.BaseException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class BaseExceptionHandler(
    private val featureCode: FeatureCode = FeatureCode.UNKNOWN
) {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponseEntity(featureCode, e.errorCode)
    }

    @ExceptionHandler(Exception::class)
    protected fun exceptionHandler(e: Exception): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponseEntity(featureCode, ErrorCode.UNKNOWN_ERROR, e.message)
    }

}