package me.jimmyberg.ams.domain.port.outbound

import me.jimmyberg.ams.common.domain.ScrollContent
import me.jimmyberg.ams.common.model.PageableRequest
import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate

interface StudentRepository {

    fun save(student: Student): Student

    fun findByPredicate(predicate: StudentPredicate): Student?

    fun findAllByPredicate(predicate: StudentPredicate, pageable: PageableRequest = PageableRequest()): List<Student>

    fun scrollByPredicate(predicate: StudentPredicate, pageable: PageableRequest = PageableRequest()): ScrollContent<Student>

    fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birth: String): Boolean

}