package me.jimmyberg.ams.student.document.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "student")
class StudentDocument(
    @Id
    private val id: String,
    private val name: String
) {
    fun getName() = this.name
}