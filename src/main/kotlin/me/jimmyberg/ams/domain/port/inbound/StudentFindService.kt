package me.jimmyberg.ams.domain.port.inbound

import me.jimmyberg.ams.domain.common.ScrollResult
import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate

interface StudentFindService {

    fun findOne(predicate: StudentPredicate): Student

    fun scroll(predicate: StudentPredicate): ScrollResult<Student>

}