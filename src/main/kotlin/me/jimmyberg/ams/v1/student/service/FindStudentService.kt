package me.jimmyberg.ams.v1.student.service

import me.jimmyberg.ams.common.domain.ScrollContent
import me.jimmyberg.ams.common.model.PageableRequest
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import me.jimmyberg.ams.v1.student.service.domain.Student

interface FindStudentService {

    fun findOne(predicate: StudentPredicate): Student

    fun scroll(predicate: StudentPredicate, pageable: PageableRequest): ScrollContent<Student>

}