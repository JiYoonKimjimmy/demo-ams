package me.jimmyberg.ams.infrastructure.error

enum class FeatureCode(
    val code: String,
    val message: String
) {

    STUDENT_MANAGEMENT_SERVICE("1000", "Student Management Service"),

    UNKNOWN("9999", "Unknown Service"),

}