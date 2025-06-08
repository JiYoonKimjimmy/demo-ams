package me.jimmyberg.ams.domain.port.inbound

import me.jimmyberg.ams.domain.model.Student

interface StudentValidateService {

    fun validate(student: Student)

}