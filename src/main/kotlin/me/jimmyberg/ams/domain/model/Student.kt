package me.jimmyberg.ams.domain.model

import me.jimmyberg.ams.domain.model.Address
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.ActivationStatus

data class Student(
    val id: Long? = null,
    val name: String,
    var nameLabel: Int? = null,
    val phone: String,
    val birth: String,
    val gender: Gender,
    val address: Address? = null,
    val school: School? = null,
    val status: ActivationStatus = ActivationStatus.REGISTER_WAITING
)