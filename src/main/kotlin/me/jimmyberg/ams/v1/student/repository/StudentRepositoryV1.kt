package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.v1.student.repository.document.StudentDocumentV1
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import org.springframework.data.domain.Pageable

interface StudentRepositoryV1 {

    fun save(document: StudentDocumentV1): StudentDocumentV1

    fun findById(id: String): StudentDocumentV1

    fun findAllByName(name: String): List<StudentDocumentV1>

    fun findAll(): List<StudentDocumentV1>

    fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birthDate: String): Boolean

    fun findByPredicate(predicate: StudentPredicate): StudentDocumentV1

    fun findAllByPredicate(predicate: StudentPredicate, pageable: Pageable): List<StudentDocumentV1>
}