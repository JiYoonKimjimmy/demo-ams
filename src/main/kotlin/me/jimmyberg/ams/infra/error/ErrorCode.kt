package me.jimmyberg.ams.infra.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    var message: String
) {

    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "001", "Student not found"),

    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "999", "Unknown error"),

}