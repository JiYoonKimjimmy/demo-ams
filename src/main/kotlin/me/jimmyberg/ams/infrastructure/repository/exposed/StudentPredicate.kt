package me.jimmyberg.ams.infrastructure.repository.exposed

import me.jimmyberg.ams.infrastructure.common.enumerate.ActivationStatus
import me.jimmyberg.ams.infrastructure.common.enumerate.Gender
import me.jimmyberg.ams.infrastructure.common.enumerate.SchoolType
import me.jimmyberg.ams.infrastructure.repository.exposed.entity.StudentTable
import me.jimmyberg.ams.presentation.common.PageableRequest
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and

data class StudentPredicate(
    val id: Long? = null,
    val name: String? = null,
    val phone: String? = null,
    val birth: String? = null,
    val gender: Gender? = null,
    val school: SchoolPredicate? = null,
    val status: ActivationStatus? = null,
    val pageable: PageableRequest = PageableRequest(),
) {

    data class SchoolPredicate(
        val schoolName: String? = null,
        val schoolType: SchoolType? = null,
        val grade: Int? = null
    )

    fun conditions(default: Op<Boolean>? = null): Op<Boolean> {
        return Op.build {
            val conditions = ArrayList<Op<Boolean>>()

            this@StudentPredicate.id?.let { conditions.add(StudentTable.id eq it) }
            this@StudentPredicate.name?.let { conditions.add(StudentTable.name eq it) }
            this@StudentPredicate.phone?.let { conditions.add(StudentTable.phone eq it) }
            this@StudentPredicate.birth?.let { conditions.add(StudentTable.birth eq it) }
            this@StudentPredicate.gender?.let { conditions.add(StudentTable.gender eq it) }
            this@StudentPredicate.school?.schoolName?.let { conditions.add(StudentTable.schoolName eq it) }
            this@StudentPredicate.school?.schoolType?.let { conditions.add(StudentTable.schoolType eq it) }
            this@StudentPredicate.school?.grade?.let { conditions.add(StudentTable.grade eq it) }
            this@StudentPredicate.status?.let { conditions.add(StudentTable.status eq it) }

            if (conditions.isEmpty()) {
                default ?: Op.FALSE
            } else {
                conditions.reduce { acc, op -> acc and op }
            }
        }
    }

}