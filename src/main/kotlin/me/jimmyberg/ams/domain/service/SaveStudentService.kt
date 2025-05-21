package me.jimmyberg.ams.domain.service

import me.jimmyberg.ams.domain.model.Student

interface SaveStudentService {

    fun save(student: Student): Student

}