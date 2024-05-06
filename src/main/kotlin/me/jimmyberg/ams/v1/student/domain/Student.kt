package me.jimmyberg.ams.v1.student.domain

import me.jimmyberg.ams.common.domain.Address
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus

data class Student(
    val id: String? = null,
    val name: String,
    var indexOfName: Int? = null,
    val phone: String,
    val birth: String,
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