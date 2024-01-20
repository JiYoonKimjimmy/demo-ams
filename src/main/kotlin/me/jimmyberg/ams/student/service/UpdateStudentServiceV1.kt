package me.jimmyberg.ams.student.service

import me.jimmyberg.ams.student.controller.model.StudentModel
import me.jimmyberg.ams.student.domain.Student

interface UpdateStudentServiceV1 {

    fun update(student: Student): StudentModel

}