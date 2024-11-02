package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.v1.student.repository.document.StudentDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface StudentMongoRepository : MongoRepository<StudentDocument, String> {

    fun findAllByName(name: String): List<StudentDocument>

    fun existsByNameAndPhoneAndBirth(name: String, phone: String, birth: String): Boolean

}