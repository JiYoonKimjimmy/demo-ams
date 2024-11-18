package me.jimmyberg.ams.v1.student.repository.document

import me.jimmyberg.ams.common.document.BaseDocument
import me.jimmyberg.ams.common.domain.Address
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.v1.student.service.domain.Student.School
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "student")
class StudentDocument(
    @Id
    override var id: String? = null,
    val name: String,
    val indexOfName: Int?,
    val phone: String,
    val birth: String,
    val gender: Gender,
    val address: Address?,
    val school: School,
    val status: StudentStatus,
) : BaseDocument()