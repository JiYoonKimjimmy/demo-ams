package me.jimmyberg.ams.v1.student.repository.predicate

import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.mongodsl.extension.document
import me.jimmyberg.ams.mongodsl.extension.field
import me.jimmyberg.ams.v1.student.repository.document.StudentDocumentV1
import me.jimmyberg.ams.v1.student.service.domain.School
import org.springframework.data.mongodb.core.query.BasicQuery

class StudentPredicate(
    val name: String? = null,
    val phone: String? = null,
    val birth: String? = null,
    val gender: Gender? = null,
    val school: School? = null,
    val status: StudentStatus? = null,
) {

    val query: BasicQuery by lazy {
        document {
            this@StudentPredicate.name?.let { and({ field(StudentDocumentV1::name) eq it }) }
            this@StudentPredicate.phone?.let { and({ field(StudentDocumentV1::phone) eq it }) }
            this@StudentPredicate.birth?.let { and({ field(StudentDocumentV1::birth) eq it }) }
            this@StudentPredicate.gender?.let { and({ field(StudentDocumentV1::gender) eq it }) }
            this@StudentPredicate.school?.let { and({ field(StudentDocumentV1::school) eq it }) }
            this@StudentPredicate.status?.let { and({ field(StudentDocumentV1::status) eq it }) }
        }
    }

}