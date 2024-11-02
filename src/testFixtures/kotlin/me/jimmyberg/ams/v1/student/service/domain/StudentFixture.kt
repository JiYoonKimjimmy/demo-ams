package me.jimmyberg.ams.v1.student.service.domain

import me.jimmyberg.ams.common.domain.Address
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.v1.student.service.domain.Student.School

class StudentFixture {

    fun make(
        name: String = "김모건",
        indexOfName: Int? = null,
        phone: String = "01012340001",
        birth: String = "19900309",
        gender: Gender = Gender.MALE,
        address: Address = Address("12345", "Hello", "World"),
        school: School = School("신길초", SchoolType.PRIMARY, 6),
        status: StudentStatus = StudentStatus.REGISTER_WAITING,
    ): Student {
        return Student(
            name = name,
            indexOfName = indexOfName,
            phone = phone,
            birth = birth,
            gender = gender,
            address = address,
            school = school,
            status = status,
        )
    }

}