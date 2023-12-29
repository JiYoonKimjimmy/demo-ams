package me.jimmyberg.ams.student.repository.mongo

import me.jimmyberg.ams.student.document.mongo.StudentDocumentV1
import org.springframework.data.mongodb.repository.MongoRepository

interface StudentMongoRepository : MongoRepository<StudentDocumentV1, String> {

    fun findByName(name: String): StudentDocumentV1?

}