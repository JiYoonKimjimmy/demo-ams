package me.jimmyberg.ams.student.document.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "student")
data class StudentDocumentV1(
    @Id
    val id: String? = null,
    val name: String
)