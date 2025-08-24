package me.jimmyberg.ams.application.model

import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import me.jimmyberg.ams.domain.model.Address
import me.jimmyberg.ams.domain.model.School
import me.jimmyberg.ams.infrastructure.common.EMPTY
import me.jimmyberg.ams.infrastructure.common.enumerate.ActivationStatus
import me.jimmyberg.ams.infrastructure.common.enumerate.Gender
import me.jimmyberg.ams.infrastructure.common.enumerate.SchoolType
import me.jimmyberg.ams.testsupport.TestExtensionFunctions.fixtureMonkey

class StudentModelFixture {

    fun make(
        name: String = "김모건",
        indexOfName: Int? = null,
        phone: String = "01012340001",
        birth: String = "19900309",
        gender: Gender = Gender.MALE,
        address: Address = Address("12345", "Hello", "World"),
        school: School = School("신길초", SchoolType.PRIMARY, 6),
        status: ActivationStatus = ActivationStatus.REGISTER_WAITING,
    ): StudentModel {
        return fixtureMonkey.giveMeKotlinBuilder<StudentModel>()
            .setExp(StudentModel::name, "$name${indexOfName ?: EMPTY}")
            .setExp(StudentModel::phone, phone)
            .setExp(StudentModel::birth, birth)
            .setExp(StudentModel::gender, gender)
            .setExp(StudentModel::zipCode, address.zipCode)
            .setExp(StudentModel::baseAddress, address.baseAddress)
            .setExp(StudentModel::detailAddress, address.detailAddress)
            .setExp(StudentModel::schoolName, school.schoolName)
            .setExp(StudentModel::schoolType, school.schoolType)
            .setExp(StudentModel::grade, school.grade)
            .setExp(StudentModel::status, status)
            .sample()
    }

}
