package me.jimmyberg.ams.student.controller.model

import me.jimmyberg.ams.student.domain.Student

data class FindAllStudentResponse(
    val data: List<Student>
)