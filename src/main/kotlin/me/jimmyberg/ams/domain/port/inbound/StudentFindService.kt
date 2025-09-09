package me.jimmyberg.ams.domain.port.inbound

import me.jimmyberg.ams.common.model.ScrollResult
import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.domain.model.predicate.StudentPredicate

interface StudentFindService {

    fun findOne(predicate: StudentPredicate): Student

    fun scroll(predicate: StudentPredicate): ScrollResult<Student>

}