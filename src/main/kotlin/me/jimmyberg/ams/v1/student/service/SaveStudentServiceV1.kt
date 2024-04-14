package me.jimmyberg.ams.v1.student.service

import me.jimmyberg.ams.v1.student.controller.model.StudentModel
import me.jimmyberg.ams.v1.student.domain.Student

interface SaveStudentServiceV1 {

    fun save(student: Student): StudentModel

}