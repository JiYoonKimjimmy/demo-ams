package me.jimmyberg.ams.v1.student.service.domain

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
    val status: StudentStatus = StudentStatus.REGISTER_WAITING
)

class School(
    val schoolName: String,
    val schoolType: SchoolType,
    val grade: Int,
) {

    override fun equals(other: Any?): Boolean {
        return if (other is School) {
            this.schoolName == other.schoolName && this.schoolType == other.schoolType && this.grade == other.grade
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return this.hashCode()
    }

}