package me.jimmyberg.ams.student.controller.model

import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus

data class SaveStudentRequest(
    val name: String,
    val phone: String,
    val birthday: String,
    val gender: Gender,
    val address: String? = null,
    val schoolName: String,
    val schoolType: SchoolType,
    val grade: Int,
    val status: StudentStatus = StudentStatus.REGISTER_WAITING
)