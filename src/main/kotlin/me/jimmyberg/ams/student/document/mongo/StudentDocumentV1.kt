package me.jimmyberg.ams.student.document.mongo

import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "student")
data class StudentDocumentV1(
    @Id
    val id: String? = null,
    val name: String,
    val phone: String,
    val birthday: String,
    @Enumerated(EnumType.STRING)
    val gender: Gender,
    val address: String? = null,
    val schoolName: String,
    @Enumerated(EnumType.STRING)
    val schoolType: SchoolType,
    val grade: Int,
    @Enumerated(EnumType.STRING)
    val status: StudentStatus = StudentStatus.REGISTER_WAITING
)