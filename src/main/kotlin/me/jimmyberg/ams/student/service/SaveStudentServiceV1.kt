package me.jimmyberg.ams.student.service

import me.jimmyberg.ams.student.controller.model.StudentModel
import me.jimmyberg.ams.student.domain.Student

interface SaveStudentServiceV1 {

    fun save(student: Student): StudentModel

}