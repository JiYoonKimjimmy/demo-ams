package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import me.jimmyberg.ams.v1.student.service.domain.Student
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

interface StudentRepository {

    fun save(domain: Student): Student

    fun findAll(): List<Student>

    fun findByPredicate(predicate: StudentPredicate): Student?

    fun findAllByPredicate(predicate: StudentPredicate, pageable: Pageable = PageRequest.of(0, 1000)): List<Student>

    fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birth: String): Boolean

}