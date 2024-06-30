package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.v1.student.repository.document.StudentDocumentV1

interface StudentRepositoryV1 {

    fun save(document: StudentDocumentV1): StudentDocumentV1

    fun findById(id: String): StudentDocumentV1

    fun findAllByName(name: String): List<StudentDocumentV1>

    fun findAll(): List<StudentDocumentV1>

    fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birthDate: String): Boolean

}