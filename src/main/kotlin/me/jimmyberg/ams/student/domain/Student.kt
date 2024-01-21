package me.jimmyberg.ams.student.domain

import me.jimmyberg.ams.common.domain.Address
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus

data class Student(
    val id: String? = null,
    val name: String,
    val phone: String,
    val birthday: String,
    val gender: Gender,
    val address: Address? = null,
    val school: School,
    val status: StudentStatus
)

class School(
    val schoolName: String,
    val schoolType: SchoolType,
    val grade: Int,
)