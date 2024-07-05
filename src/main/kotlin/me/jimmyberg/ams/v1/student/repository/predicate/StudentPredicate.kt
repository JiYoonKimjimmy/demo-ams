package me.jimmyberg.ams.v1.student.repository.predicate

import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.v1.student.service.domain.School

class StudentPredicate(
    val name: String? = null,
    val phone: String? = null,
    val birth: String? = null,
    val gender: Gender? = null,
    val school: School? = null,
    val status: StudentStatus? = null,
)