package me.jimmyberg.ams.domain.port.outbound

import me.jimmyberg.ams.infrastructure.common.domain.ScrollResult
import me.jimmyberg.ams.infrastructure.common.model.PageableRequest
import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate

interface StudentRepository {

    fun save(student: Student): Student

    fun findByPredicate(predicate: StudentPredicate): Student?

    fun findAllByPredicate(predicate: StudentPredicate, pageable: PageableRequest = PageableRequest()): List<Student>

    fun scrollByPredicate(predicate: StudentPredicate, pageable: PageableRequest = PageableRequest()): ScrollResult<Student>

    fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birth: String): Boolean

}