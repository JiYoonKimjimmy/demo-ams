package me.jimmyberg.ams.v1.student.repository.predicate

import me.jimmyberg.ams.common.enumerate.ActivationStatus
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.mongodsl.extension.document
import me.jimmyberg.ams.mongodsl.extension.field
import me.jimmyberg.ams.v1.student.repository.document.StudentDocument
import me.jimmyberg.ams.v1.student.repository.entity.StudentTable
import me.jimmyberg.ams.v1.student.service.domain.Student.School
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
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

    fun conditions(): Op<Boolean> {
        return Op.build {
            val conditions = ArrayList<Op<Boolean>>()
            
            this@StudentPredicate.id?.let { conditions.add(StudentTable.id eq it.toLong()) }
            this@StudentPredicate.name?.let { conditions.add(StudentTable.name eq it) }
            this@StudentPredicate.phone?.let { conditions.add(StudentTable.phone eq it) }
            this@StudentPredicate.birth?.let { conditions.add(StudentTable.birth eq it) }
            this@StudentPredicate.gender?.let { conditions.add(StudentTable.gender eq it) }
            this@StudentPredicate.school?.schoolName?.let { conditions.add(StudentTable.schoolName eq it) }
            this@StudentPredicate.school?.schoolType?.let { conditions.add(StudentTable.schoolType eq it) }
            this@StudentPredicate.school?.grade?.let { conditions.add(StudentTable.grade eq it) }
            this@StudentPredicate.status?.let { conditions.add(StudentTable.status eq it) }
            
            if (conditions.isEmpty()) {
                Op.FALSE
            } else {
                conditions.reduce { acc, op -> acc and op }
            }
        }
    }

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