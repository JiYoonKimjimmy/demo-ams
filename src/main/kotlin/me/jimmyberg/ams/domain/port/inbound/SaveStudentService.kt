package me.jimmyberg.ams.domain.port.inbound

import me.jimmyberg.ams.domain.model.Student

interface SaveStudentService {

    fun save(student: Student): Student

}