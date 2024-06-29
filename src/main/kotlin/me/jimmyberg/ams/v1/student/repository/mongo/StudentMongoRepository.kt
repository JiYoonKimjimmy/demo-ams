package me.jimmyberg.ams.v1.student.repository.mongo

import me.jimmyberg.ams.v1.student.document.mongo.StudentDocumentV1
import org.springframework.data.mongodb.repository.MongoRepository

interface StudentMongoRepository : MongoRepository<StudentDocumentV1, String> {

    fun findAllByName(name: String): List<StudentDocumentV1>

    fun existsByNameAndPhoneAndBirth(name: String, phone: String, birth: String): Boolean

}