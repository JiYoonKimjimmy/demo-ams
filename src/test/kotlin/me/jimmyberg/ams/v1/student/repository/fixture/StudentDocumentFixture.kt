package me.jimmyberg.ams.v1.student.repository.fixture

import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.v1.student.repository.document.StudentDocumentV1
import me.jimmyberg.ams.v1.student.service.domain.School
import java.util.UUID

class StudentDocumentFixture {

    companion object {
        val documents = mutableListOf(
            StudentDocumentV1(
                id = UUID.randomUUID().toString(),
                name = "김모건",
                phone = "01012340001",
                birth = "19900309",
                gender = Gender.MALE,
                school = School("신길초", SchoolType.PRIMARY, 6),
                status = StudentStatus.ACTIVATED,
            ),
            StudentDocumentV1(
                id = UUID.randomUUID().toString(),
                name = "김모아",
                phone = "01012340002",
                birth = "19900202",
                gender = Gender.FEMALE,
                school = School("여의도중", SchoolType.MIDDLE, 1),
                status = StudentStatus.ACTIVATED,
            ),
            StudentDocumentV1(
                id = UUID.randomUUID().toString(),
                name = "김모군",
                phone = "01012340001",
                birth = "19900309",
                gender = Gender.MALE,
                school = School("신길초", SchoolType.PRIMARY, 6),
                status = StudentStatus.ACTIVATED,
            )
        )
    }

    fun make(
        name: String = "김모건",
        phone: String = "01012340001",
        birth: String = "19900309",
        gender: Gender = Gender.MALE,
        school: School = School("신길초", SchoolType.PRIMARY, 6),
        status: StudentStatus = StudentStatus.ACTIVATED,
    ): StudentDocumentV1 {
        return StudentDocumentV1(
            name = name,
            phone = phone,
            birth = birth,
            gender = gender,
            school = school,
            status = status,
        )
    }

}