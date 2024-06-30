package me.jimmyberg.ams.v1.student.service

import me.jimmyberg.ams.v1.student.service.domain.Student

interface UpdateStudentServiceV1 {

    fun update(student: Student): Student

}