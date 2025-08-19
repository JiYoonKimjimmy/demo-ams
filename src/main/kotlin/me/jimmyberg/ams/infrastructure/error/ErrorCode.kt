package me.jimmyberg.ams.infrastructure.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    val message: String
) {

    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "001", "Student not found"),
    STUDENT_INFO_DUPLICATED(HttpStatus.BAD_REQUEST, "002", "Student with same name, phone, and birth already exists"),

    REQUIRED_NAME(HttpStatus.BAD_REQUEST, "801", "Name is required"),
    REQUIRED_PHONE(HttpStatus.BAD_REQUEST, "802", "Phone is required"),
    REQUIRED_BIRTH(HttpStatus.BAD_REQUEST, "803", "Birth is required"),
    REQUIRED_GENDER(HttpStatus.BAD_REQUEST, "804", "Gender is required"),

    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "999", "Unknown error"),

}