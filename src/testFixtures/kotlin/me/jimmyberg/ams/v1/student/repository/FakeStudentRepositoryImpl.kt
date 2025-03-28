package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.common.domain.ScrollContent
import me.jimmyberg.ams.common.model.PageableRequest
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import me.jimmyberg.ams.v1.student.service.domain.Student

class FakeStudentRepositoryImpl : StudentRepository {

    override fun save(student: Student): Student {
        TODO("Not yet implemented")
    }

    override fun findByPredicate(predicate: StudentPredicate): Student? {
        TODO("Not yet implemented")
    }

    override fun findAllByPredicate(
        predicate: StudentPredicate,
        pageable: PageableRequest
    ): List<Student> {
        TODO("Not yet implemented")
    }

    override fun scrollByPredicate(
        predicate: StudentPredicate,
        pageable: PageableRequest
    ): ScrollContent<Student> {
        TODO("Not yet implemented")
    }

    override fun isExistByNameAndPhoneAndBirth(
        name: String,
        phone: String,
        birth: String
    ): Boolean {
        TODO("Not yet implemented")
    }

}