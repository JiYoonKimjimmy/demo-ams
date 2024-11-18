package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.testsupport.TestExtensionFunctions.ifNotNullEquals
import me.jimmyberg.ams.common.domain.PageableContent
import me.jimmyberg.ams.common.model.PageableRequest
import me.jimmyberg.ams.testsupport.FakeBaseRepository
import me.jimmyberg.ams.v1.student.repository.document.StudentDocument
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import me.jimmyberg.ams.v1.student.service.domain.Student
import me.jimmyberg.ams.v1.student.service.domain.StudentMapper

class FakeStudentRepositoryImpl(
    private val studentMapper: StudentMapper
) : StudentRepository, FakeBaseRepository<StudentDocument>() {

    fun clear() = super.documents.clear()

    override fun save(student: Student): Student {
        return studentMapper.domainToDocument(student)
            .let { super.save(it) }
            .let { studentMapper.documentToDomain(it) }
    }

    override fun findAll(): List<Student> {
        return super.documents.values.map { studentMapper.documentToDomain(it) }
    }

    override fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birth: String): Boolean {
        return super.documents.values.any {
            name.ifNotNullEquals(it.name)
                && phone.ifNotNullEquals(it.phone)
                && birth.ifNotNullEquals(it.birth)
        }
    }

    override fun findByPredicate(predicate: StudentPredicate): Student? {
        return super.documents.values
            .find { filterByPredicate(predicate, it) }
            ?.let { studentMapper.documentToDomain(it) }
    }

    override fun findAllByPredicate(predicate: StudentPredicate, pageable: PageableRequest): List<Student> {
        return super.documents.values
            .filter { filterByPredicate(predicate, it) }
            .map { studentMapper.documentToDomain(it) }
    }

    override fun scrollByPredicate(
        predicate: StudentPredicate,
        pageable: PageableRequest
    ): PageableContent<Student> {
        val total = super.documents.values.filter { filterByPredicate(predicate, it) }
        val start = pageable.number * pageable.size
        val last = ((pageable.number + 1) * pageable.size) - 1
        val end = if (total.size < last) total.size else last
        val content = total.subList(start, end)
        return PageableContent(
            size = total.size,
            hasNext = (pageable.number < last),
            isLast = (pageable.number >= last),
            isEmpty = total.isEmpty(),
            content = content.map(studentMapper::documentToDomain)
        )
    }

    private fun filterByPredicate(predicate: StudentPredicate, document: StudentDocument): Boolean {
        return predicate.name.ifNotNullEquals(document.name)
            && predicate.phone.ifNotNullEquals(document.phone)
            && predicate.birth.ifNotNullEquals(document.birth)
            && predicate.gender.ifNotNullEquals(document.gender)
            && predicate.school?.schoolName.ifNotNullEquals(document.school.schoolName)
            && predicate.school?.schoolType.ifNotNullEquals(document.school.schoolType)
            && predicate.school?.grade.ifNotNullEquals(document.school.grade)
            && predicate.status.ifNotNullEquals(document.status)
    }

}