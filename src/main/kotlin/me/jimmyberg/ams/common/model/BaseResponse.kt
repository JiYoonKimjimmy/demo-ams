package me.jimmyberg.ams.common.model

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

abstract class BaseResponse<T>(
    private val result: BaseResult = BaseResult()
) {

    fun getResult() = this.result

    abstract fun success(httpStatus: HttpStatus): ResponseEntity<T>

}