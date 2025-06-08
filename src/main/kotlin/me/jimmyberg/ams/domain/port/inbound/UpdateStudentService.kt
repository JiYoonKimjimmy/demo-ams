package me.jimmyberg.ams.domain.port.inbound

import me.jimmyberg.ams.domain.model.Student

interface UpdateStudentService {

    fun update(student: Student): Student

}