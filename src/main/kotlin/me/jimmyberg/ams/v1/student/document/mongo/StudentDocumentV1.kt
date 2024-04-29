package me.jimmyberg.ams.v1.student.document.mongo

import me.jimmyberg.ams.common.domain.Address
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.v1.student.domain.School
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "student")
data class StudentDocumentV1(
    @Id
    var id: String? = null,
    val name: String,
    val phone: String,
    val birthday: String,
    val gender: Gender,
    val address: Address? = null,
    val school: School,
    val status: StudentStatus = StudentStatus.REGISTER_WAITING
)