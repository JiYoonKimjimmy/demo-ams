package me.jimmyberg.ams.infrastructure.adapter.outbound.exposed.entity

import me.jimmyberg.ams.common.enumerate.ActivationStatus
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.model.PageableRequest
import me.jimmyberg.ams.domain.model.predicate.StudentPredicate
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and

data class StudentQuery(
    val id: Long? = null,
    val name: String? = null,
    val phone: String? = null,
    val birth: String? = null,
    val gender: Gender? = null,
    val schoolName: String? = null,
    val schoolType: SchoolType? = null,
    val grade: Int? = null,
    val status: ActivationStatus? = null,
    val pageable: PageableRequest = PageableRequest(),
) {

    companion object {
        fun of(predicate: StudentPredicate): StudentQuery {
            return StudentQuery(
                id = predicate.id,
                name = predicate.name,
                phone = predicate.phone,
                birth = predicate.birth,
                gender = predicate.gender,
                schoolName = predicate.school?.schoolName,
                schoolType = predicate.school?.schoolType,
                grade = predicate.school?.grade,
                status = predicate.status,
                pageable = predicate.pageable,
            )
        }
    }

    fun conditions(default: Op<Boolean>? = null): Op<Boolean> {
        return Op.build {
            val conditions = ArrayList<Op<Boolean>>()

            this@StudentQuery.id?.let { conditions.add(StudentTable.id eq it) }
            this@StudentQuery.name?.let { conditions.add(StudentTable.name eq it) }
            this@StudentQuery.phone?.let { conditions.add(StudentTable.phone eq it) }
            this@StudentQuery.birth?.let { conditions.add(StudentTable.birth eq it) }
            this@StudentQuery.gender?.let { conditions.add(StudentTable.gender eq it) }
            this@StudentQuery.schoolName?.let { conditions.add(StudentTable.schoolName eq it) }
            this@StudentQuery.schoolType?.let { conditions.add(StudentTable.schoolType eq it) }
            this@StudentQuery.grade?.let { conditions.add(StudentTable.grade eq it) }
            this@StudentQuery.status?.let { conditions.add(StudentTable.status eq it) }

            if (conditions.isEmpty()) {
                default ?: Op.FALSE
            } else {
                conditions.reduce { acc, op -> acc and op }
            }
        }
    }

}