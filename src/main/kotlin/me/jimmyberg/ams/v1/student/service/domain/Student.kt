package me.jimmyberg.ams.v1.student.service.domain

import me.jimmyberg.ams.common.domain.Address
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.ActivationStatus

data class Student(
    val id: String? = null,
    val name: String,
    var nameLabel: Int? = null,
    val phone: String,
    val birth: String,
    val gender: Gender,
    val address: Address? = null,
    val school: School? = null,
    val status: ActivationStatus = ActivationStatus.REGISTER_WAITING
) {
    data class School(
        val schoolName: String,
        val schoolType: SchoolType,
        val grade: Int,
    )
}