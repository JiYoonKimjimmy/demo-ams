package me.jimmyberg.ams.student.controller.model

import com.fasterxml.jackson.annotation.JsonIgnore
import me.jimmyberg.ams.common.domain.Address
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.student.domain.School

data class StudentModel(
    val id: String? = null,
    val name: String,
    val phone: String,
    val birthday: String,
    val gender: Gender,
    val zipCode: String? = null,
    val baseAddress: String? = null,
    val detailAddress: String? = null,
    val schoolName: String,
    val schoolType: SchoolType,
    val grade: Int,
    val status: StudentStatus = StudentStatus.REGISTER_WAITING
) {

    @get:JsonIgnore
    val address: Address? by lazy {
        if (zipCode != null && baseAddress != null && detailAddress != null) {
            Address(zipCode, baseAddress, detailAddress)
        } else {
            null
        }
    }

    @get:JsonIgnore
    val school: School by lazy {
        School(schoolName, schoolType, grade)
    }

}