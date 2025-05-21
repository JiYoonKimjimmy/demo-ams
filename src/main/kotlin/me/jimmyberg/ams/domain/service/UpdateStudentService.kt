package me.jimmyberg.ams.domain.service

import me.jimmyberg.ams.domain.model.Student

interface UpdateStudentService {

    fun update(student: Student): Student

}