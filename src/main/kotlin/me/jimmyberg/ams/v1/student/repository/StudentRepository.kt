package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.v1.student.domain.Student

interface StudentRepository {

    fun save(domain: Student): Student

    fun findById(id: String): Student

    fun findAllByName(name: String): List<Student>

    fun findAll(): List<Student>

    fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birthDate: String): Boolean

}