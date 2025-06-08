package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.infrastructure.common.domain.ScrollResult
import me.jimmyberg.ams.infrastructure.common.model.PageableRequest
import me.jimmyberg.ams.domain.port.outbound.StudentRepository
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate
import me.jimmyberg.ams.domain.model.Student
import java.util.concurrent.atomic.AtomicLong

class FakeStudentRepositoryImpl : StudentRepository {
    private val students = mutableMapOf<Long, Student>()
    private val idSequence = AtomicLong(1L)

    override fun save(student: Student): Student {
        val saved = if (student.id == null) {
            val newId = idSequence.getAndIncrement()
            val newStudent = student.copy(id = newId)
            students[newId] = newStudent
            newStudent
        } else {
            students[student.id] = student
            student
        }
        return saved
    }

    override fun findByPredicate(predicate: StudentPredicate): Student? {
        return students.values.find { matchesPredicate(it, predicate) }
    }

    override fun findAllByPredicate(
        predicate: StudentPredicate,
        pageable: PageableRequest
    ): List<Student> {
        return students.values.filter { matchesPredicate(it, predicate) }
            .drop(pageable.offset.toInt())
            .take(pageable.size)
    }

    override fun scrollByPredicate(
        predicate: StudentPredicate,
        pageable: PageableRequest
    ): ScrollResult<Student> {
        val filtered = students.values.filter { matchesPredicate(it, predicate) }
        val paged = filtered.drop(pageable.offset.toInt()).take(pageable.size + 1)
        val hasNext = paged.size > pageable.size
        val content = if (hasNext) paged.dropLast(1) else paged
        return ScrollResult(
            size = content.size,
            isEmpty = content.isEmpty(),
            hasNext = hasNext,
            content = content
        )
    }

    override fun isExistByNameAndPhoneAndBirth(
        name: String,
        phone: String,
        birth: String
    ): Boolean {
        return students.values.any { it.name == name && it.phone == phone && it.birth == birth }
    }

    private fun matchesPredicate(student: Student, predicate: StudentPredicate): Boolean {
        return (predicate.id == null || student.id?.toString() == predicate.id) &&
                (predicate.name == null || student.name == predicate.name) &&
                (predicate.phone == null || student.phone == predicate.phone) &&
                (predicate.birth == null || student.birth == predicate.birth) &&
                (predicate.gender == null || student.gender == predicate.gender) &&
                (predicate.status == null || student.status == predicate.status) &&
                (predicate.school?.schoolName == null || student.school?.schoolName == predicate.school.schoolName) &&
                (predicate.school?.schoolType == null || student.school?.schoolType == predicate.school.schoolType) &&
                (predicate.school?.grade == null || student.school?.grade == predicate.school.grade)
    }
}