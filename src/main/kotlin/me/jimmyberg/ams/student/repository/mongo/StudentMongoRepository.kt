package me.jimmyberg.ams.student.repository.mongo

import me.jimmyberg.ams.student.document.mongo.StudentDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface StudentMongoRepository : MongoRepository<StudentDocument, String>