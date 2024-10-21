package me.jimmyberg.ams.v1.student.service

import me.jimmyberg.ams.v1.student.service.domain.Student

interface FindStudentService {

    fun findOne(id: String): Student

    fun findAll(): List<Student>

}