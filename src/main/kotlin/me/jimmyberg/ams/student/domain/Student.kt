package me.jimmyberg.ams.student.domain

data class Student(
    val id: String,
    val name: String,
    val phone: String,
    val birthday: String,
    val gender: Gender,
    val address: String?,
    val schoolName: String,
    val schoolType: SchoolType,
    val grade: Int,
    val status: StudentStatus
)

enum class SchoolType {
    PRIMARY,
    MIDDLE,
    HIGH,
    UNIVERSITY
}

enum class StudentStatus {
    REGISTER_WAITING,
    ACTIVATED,
    INACTIVATED
}

enum class Gender {
    MALE,
    FEMALE,
    NON_BINARY
}