package me.jimmyberg.ams.student.service

import me.jimmyberg.ams.student.controller.model.StudentModel

interface FindStudentServiceV1 {

    fun findOne(id: String): StudentModel
    fun findAll(): List<StudentModel>

}