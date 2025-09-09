package me.jimmyberg.ams.presentation.dto

import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import me.jimmyberg.ams.common.EMPTY
import me.jimmyberg.ams.common.enumerate.ActivationStatus
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.domain.model.Address
import me.jimmyberg.ams.domain.model.School
import me.jimmyberg.ams.testsupport.TestExtensionFunctions.fixtureMonkey

class StudentDTOFixture {

    fun make(
        name: String = "김모건",
        indexOfName: Int? = null,
        phone: String = "01012340001",
        birth: String = "19900309",
        gender: Gender = Gender.MALE,
        address: Address = Address("12345", "Hello", "World"),
        school: School = School("신길초", SchoolType.PRIMARY, 6),
        status: ActivationStatus = ActivationStatus.REGISTER_WAITING,
    ): StudentDTO {
        return fixtureMonkey.giveMeKotlinBuilder<StudentDTO>()
            .setExp(StudentDTO::name, "$name${indexOfName ?: EMPTY}")
            .setExp(StudentDTO::phone, phone)
            .setExp(StudentDTO::birth, birth)
            .setExp(StudentDTO::gender, gender)
            .setExp(StudentDTO::zipCode, address.zipCode)
            .setExp(StudentDTO::baseAddress, address.baseAddress)
            .setExp(StudentDTO::detailAddress, address.detailAddress)
            .setExp(StudentDTO::schoolName, school.schoolName)
            .setExp(StudentDTO::schoolType, school.schoolType)
            .setExp(StudentDTO::grade, school.grade)
            .setExp(StudentDTO::status, status)
            .sample()
    }

}