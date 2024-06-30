package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.v1.student.repository.document.StudentDocumentV1
import org.springframework.data.mongodb.repository.MongoRepository

interface StudentMongoRepository : MongoRepository<StudentDocumentV1, String> {

    fun findAllByName(name: String): List<StudentDocumentV1>

    fun existsByNameAndPhoneAndBirth(name: String, phone: String, birth: String): Boolean

}