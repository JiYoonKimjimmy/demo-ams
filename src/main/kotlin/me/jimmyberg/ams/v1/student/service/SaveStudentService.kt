package me.jimmyberg.ams.v1.student.service

import me.jimmyberg.ams.v1.student.service.domain.Student

interface SaveStudentService {

    fun save(student: Student): Student

}