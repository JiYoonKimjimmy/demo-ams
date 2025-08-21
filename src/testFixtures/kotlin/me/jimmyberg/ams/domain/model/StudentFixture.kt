package me.jimmyberg.ams.domain.model

import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.navercorp.fixturemonkey.kotlin.into
import me.jimmyberg.ams.infrastructure.common.enumerate.ActivationStatus
import me.jimmyberg.ams.infrastructure.common.enumerate.Gender
import me.jimmyberg.ams.infrastructure.common.enumerate.SchoolType
import me.jimmyberg.ams.testsupport.TestExtensionFunctions.fixtureMonkey
import net.jqwik.api.Arbitraries

class StudentFixture {

    fun make(
        name: String = "김모건",
        indexOfName: Int? = null,
        phone: String = "01012340001",
        birth: String = "19900309",
        gender: Gender = Gender.MALE,
        address: Address = Address("12345", "Hello", "World"),
        school: School = School("신길초", SchoolType.PRIMARY, 6),
        status: ActivationStatus = ActivationStatus.REGISTER_WAITING,
    ): Student {
        return fixtureMonkey.giveMeKotlinBuilder<Student>()
            .setExp(Student::name, name)
            .setExp(Student::nameLabel, indexOfName)
            .setExp(Student::phone, phone)
            .setExp(Student::birth, birth)
            .setExp(Student::gender, gender)
            .setExp(Student::address, address)
            .setExp(Student::school, school)
            .setExp(Student::status, status)
            .sample()
    }

    fun make(): Student {
        return fixtureMonkey.giveMeKotlinBuilder<Student>()
            .setExp(Student::phone, Arbitraries.strings().ofMaxLength(32))
            .setExp(Student::birth, Arbitraries.strings().ofMaxLength(8))
            .setExp(Student::gender, Arbitraries.of(*Gender.entries.toTypedArray()))
            .setExp(Student::address into Address::zipCode, Arbitraries.strings().ofMaxLength(6))
            .setExp(Student::school into School::schoolType, Arbitraries.of(*SchoolType.entries.toTypedArray()))
            .setExp(Student::status, Arbitraries.of(*ActivationStatus.entries.toTypedArray()))
            .sample()
    }

}