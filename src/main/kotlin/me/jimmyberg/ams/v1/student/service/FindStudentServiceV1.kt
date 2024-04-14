package me.jimmyberg.ams.v1.student.service

import me.jimmyberg.ams.v1.student.controller.model.StudentModel

interface FindStudentServiceV1 {

    fun findOne(id: String): StudentModel
    fun findAll(): List<StudentModel>

}