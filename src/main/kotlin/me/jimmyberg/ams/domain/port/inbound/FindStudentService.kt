package me.jimmyberg.ams.domain.port.inbound

import me.jimmyberg.ams.common.domain.ScrollContent
import me.jimmyberg.ams.common.model.PageableRequest
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate
import me.jimmyberg.ams.domain.model.Student

interface FindStudentService {

    fun findOne(predicate: StudentPredicate): Student

    fun scroll(predicate: StudentPredicate, pageable: PageableRequest): ScrollContent<Student>

}