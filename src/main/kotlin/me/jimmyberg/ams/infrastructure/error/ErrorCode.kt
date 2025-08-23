package me.jimmyberg.ams.infrastructure.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    val message: String
) {

    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "001", "Student not found"),
    STUDENT_INFO_DUPLICATED(HttpStatus.BAD_REQUEST, "002", "Student with same name, phone, and birth already exists"),

    MISSING_REQUIRED_DATA(HttpStatus.BAD_REQUEST, "801", "Some required data is missing"),

    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "999", "Unknown error"),

}