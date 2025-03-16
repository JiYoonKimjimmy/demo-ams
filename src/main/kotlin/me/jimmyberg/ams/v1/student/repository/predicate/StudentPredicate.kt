package me.jimmyberg.ams.v1.student.repository.predicate

import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.ActivationStatus
import me.jimmyberg.ams.mongodsl.extension.document
import me.jimmyberg.ams.mongodsl.extension.field
import me.jimmyberg.ams.v1.student.repository.document.StudentDocument
import me.jimmyberg.ams.v1.student.service.domain.Student.School
import org.springframework.data.mongodb.core.query.BasicQuery

data class StudentPredicate(
    val id: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val birth: String? = null,
    val gender: Gender? = null,
    val school: SchoolPredicate? = null,
    val status: ActivationStatus? = null,
) {

    data class SchoolPredicate(
        val schoolName: String? = null,
        val schoolType: SchoolType? = null,
        val grade: Int? = null
    )

    val query: BasicQuery by lazy {
        document {
            this@StudentPredicate.id?.let { and({ field(StudentDocument::id) eq it }) }
            this@StudentPredicate.name?.let { and({ field(StudentDocument::name) eq it }) }
            this@StudentPredicate.phone?.let { and({ field(StudentDocument::phone) eq it }) }
            this@StudentPredicate.birth?.let { and({ field(StudentDocument::birth) eq it }) }
            this@StudentPredicate.gender?.let { and({ field(StudentDocument::gender) eq it }) }
            this@StudentPredicate.school?.schoolName?.let { and({ field(School::schoolName) eq it }) }
            this@StudentPredicate.school?.schoolType?.let { and({ field(School::schoolType) eq it }) }
            this@StudentPredicate.school?.grade?.let { and({ field(School::grade) eq it }) }
            this@StudentPredicate.status?.let { and({ field(StudentDocument::status) eq it }) }
        }
    }

}