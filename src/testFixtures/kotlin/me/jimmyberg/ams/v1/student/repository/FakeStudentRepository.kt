package me.jimmyberg.ams.v1.student.repository

import jakarta.persistence.EntityNotFoundException
import me.jimmyberg.ams.common.domain.Address
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.v1.student.repository.document.StudentDocumentFixture
import me.jimmyberg.ams.v1.student.repository.document.StudentDocumentV1
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import me.jimmyberg.ams.v1.student.service.domain.School
import org.springframework.data.domain.Pageable
import java.util.*

class FakeStudentRepository : StudentRepositoryV1 {

    private val documents = StudentDocumentFixture().documents

    fun clear() = documents.clear()

    fun save(
        name: String = "김모건",
        indexOfName: Int? = null,
        phone: String = "01012340001",
        birth: String = "19900309",
        gender: Gender = Gender.MALE,
        address: Address = Address("12345", "Hello", "World"),
        school: School = School("신길초", SchoolType.PRIMARY, 6),
        status: StudentStatus = StudentStatus.REGISTER_WAITING,
    ): StudentDocumentV1 {
        val document = StudentDocumentV1(
            name = name,
            indexOfName = indexOfName,
            phone = phone,
            birth = birth,
            gender = gender,
            address = address,
            school = school,
            status = status
        )
        return save(document)
    }

    override fun save(document: StudentDocumentV1): StudentDocumentV1 {
        documents += document.apply { id = UUID.randomUUID().toString() }
        return document
    }

    override fun findById(id: String): StudentDocumentV1 {
        return documents.find { it.id == id } ?: throw EntityNotFoundException()
    }

    override fun findAllByName(name: String): List<StudentDocumentV1> {
        return documents.filter { it.name == name }
    }

    override fun findAll(): List<StudentDocumentV1> {
        return documents
    }

    override fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birthDate: String): Boolean {
        return documents.any { it.name == name && it.phone == phone && it.birth == birthDate }
    }

    override fun findByPredicate(predicate: StudentPredicate): StudentDocumentV1 {
        return documents.find { document ->
            predicate.name?.let { document.name == it } ?: true
            && predicate.phone?.let { document.phone == it } ?: true
            && predicate.birth?.let { document.birth == it } ?: true
            && predicate.gender?.let { document.gender == it } ?: true
            && predicate.school?.let { document.school == it } ?: true
            && predicate.status?.let { document.status == it } ?: true
        } ?: throw EntityNotFoundException("Student not found.")
    }

    override fun findAllByPredicate(predicate: StudentPredicate, pageable: Pageable): List<StudentDocumentV1> {
        return documents.filter { document ->
            predicate.name?.let { document.name == it } ?: true
            && predicate.phone?.let { document.phone == it } ?: true
            && predicate.birth?.let { document.birth == it } ?: true
            && predicate.gender?.let { document.gender == it } ?: true
            && predicate.school?.let { document.school == it } ?: true
            && predicate.status?.let { document.status == it } ?: true
        }
    }

}