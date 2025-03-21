package me.jimmyberg.ams.common.model

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

abstract class BaseResponse<T>(
    val result: BaseResult = BaseResult()
) {
    abstract fun success(httpStatus: HttpStatus): ResponseEntity<T>
}