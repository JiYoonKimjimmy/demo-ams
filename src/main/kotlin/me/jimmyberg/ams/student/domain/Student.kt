package me.jimmyberg.ams.student.domain

data class Student(
    private val name: String
) {
    fun getName() = this.name
}