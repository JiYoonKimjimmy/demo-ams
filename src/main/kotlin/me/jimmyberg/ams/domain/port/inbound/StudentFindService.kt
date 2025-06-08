package me.jimmyberg.ams.domain.port.inbound

import me.jimmyberg.ams.domain.common.ScrollResult
import me.jimmyberg.ams.presentation.common.PageableRequest
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate
import me.jimmyberg.ams.domain.model.Student

interface StudentFindService {

    fun findOne(predicate: StudentPredicate): Student

    fun scroll(predicate: StudentPredicate, pageable: PageableRequest): ScrollResult<Student>

}