package me.jimmyberg.ams.domain.port.outbound

import me.jimmyberg.ams.common.model.ScrollResult
import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.domain.model.predicate.StudentPredicate

interface StudentRepository {

    fun save(student: Student): Student

    fun findByPredicate(predicate: StudentPredicate): Student?

    fun findAllByPredicate(predicate: StudentPredicate): List<Student>

    fun scrollByPredicate(predicate: StudentPredicate): ScrollResult<Student>

    fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birth: String): Boolean

    fun findMaxNameLabelByName(name: String): Int?

    fun isExistByNameAndPhoneAndBirthExceptId(name: String, phone: String, birth: String, excludeId: Long): Boolean

}