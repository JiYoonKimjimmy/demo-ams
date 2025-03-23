package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.common.domain.ScrollContent
import me.jimmyberg.ams.common.model.PageableRequest
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import me.jimmyberg.ams.v1.student.service.domain.Student

interface StudentRepository {

    fun save(student: Student): Student

    fun findByPredicate(predicate: StudentPredicate): Student?

    fun findAllByPredicate(predicate: StudentPredicate, pageable: PageableRequest = PageableRequest()): List<Student>

    fun scrollByPredicate(predicate: StudentPredicate, pageable: PageableRequest = PageableRequest()): ScrollContent<Student>

    fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birth: String): Boolean

}