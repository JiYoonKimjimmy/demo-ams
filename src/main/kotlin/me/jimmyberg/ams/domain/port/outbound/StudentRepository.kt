package me.jimmyberg.ams.domain.port.outbound

import me.jimmyberg.ams.domain.common.ScrollResult
import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate

interface StudentRepository {

    fun save(student: Student): Student

    fun findByPredicate(predicate: StudentPredicate): Student?

    fun findAllByPredicate(predicate: StudentPredicate): List<Student>

    fun scrollByPredicate(predicate: StudentPredicate): ScrollResult<Student>

    fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birth: String): Boolean

}