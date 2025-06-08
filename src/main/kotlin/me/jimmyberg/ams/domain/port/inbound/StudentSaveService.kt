package me.jimmyberg.ams.domain.port.inbound

import me.jimmyberg.ams.domain.model.Student

interface StudentSaveService {

    fun save(student: Student): Student

}