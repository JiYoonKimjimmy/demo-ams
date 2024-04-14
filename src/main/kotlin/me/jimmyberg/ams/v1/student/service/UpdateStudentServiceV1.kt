package me.jimmyberg.ams.v1.student.service

import me.jimmyberg.ams.v1.student.controller.model.StudentModel
import me.jimmyberg.ams.v1.student.domain.Student

interface UpdateStudentServiceV1 {

    fun update(student: Student): StudentModel

}