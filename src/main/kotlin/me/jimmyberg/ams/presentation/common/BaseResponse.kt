package me.jimmyberg.ams.presentation.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

abstract class BaseResponse<T>(
    val result: BaseResult = BaseResult()
) {
    abstract fun success(httpStatus: HttpStatus): ResponseEntity<T>
}