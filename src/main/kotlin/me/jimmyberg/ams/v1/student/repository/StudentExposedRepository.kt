package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.common.model.PageableRequest
import me.jimmyberg.ams.infra.error.ErrorCode
import me.jimmyberg.ams.infra.error.exception.ResourceNotFoundException
import me.jimmyberg.ams.v1.student.repository.entity.StudentEntity
import me.jimmyberg.ams.v1.student.repository.entity.StudentTable
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import me.jimmyberg.ams.v1.student.service.domain.Student
import org.jetbrains.exposed.sql.Op
import org.springframework.stereotype.Repository

@Repository
class StudentExposedRepository {

    fun save(domain: Student): StudentEntity {
        return StudentEntity.new {
            name = domain.name
            nameLabel = domain.nameLabel?.toString()
            phone = domain.phone
            birth = domain.birth
            gender = domain.gender
            zipCode = domain.address?.zipCode
            baseAddress = domain.address?.baseAddress
            detailAddress = domain.address?.detailAddress
            schoolName = domain.school?.schoolName
            schoolType = domain.school?.schoolType
            grade = domain.school?.grade
            status = domain.status
        }
    }

    fun findById(id: Long): StudentEntity? {
        return StudentEntity.findById(id)
    }

    fun findByPredicate(predicate: StudentPredicate): StudentEntity? {
        return StudentEntity.find(predicate.conditions()).singleOrNull()
    }

    fun findAllByPredicate(predicate: StudentPredicate): List<StudentEntity> {
        return StudentEntity.find(predicate.conditions()).toList()
    }

    fun scrollByPredicate(predicate: StudentPredicate, pageable: PageableRequest): Pair<List<StudentEntity>, Boolean> {
        val sortOrder = when (pageable.sortBy) {
            "id" -> StudentTable.id
            else -> StudentTable.id
        }

        val entities = StudentEntity.find(predicate.conditions(Op.TRUE))
            .orderBy(sortOrder to pageable.sortOrder)
            .limit(pageable.size + 1)
            .offset(pageable.offset)
            .toList()

        val hasNext = entities.size > pageable.size
        val result = if (hasNext) entities.dropLast(1) else entities

        return Pair(result, hasNext)
    }

    fun findAllByNameAndPhoneAndBirth(name: String, phone: String, birth: String): List<StudentEntity> {
        return StudentEntity.find {
            StudentTable.name eq name
            StudentTable.phone eq phone
            StudentTable.birth eq birth
        }.toList()
    }

    fun update(domain: Student): StudentEntity {
        return StudentEntity.findByIdAndUpdate(domain.id!!.toLong()) {
            it.name = domain.name
            it.nameLabel = domain.nameLabel?.toString()
            it.phone = domain.phone
            it.birth = domain.birth
            it.gender = domain.gender
            it.zipCode = domain.address?.zipCode
            it.baseAddress = domain.address?.baseAddress
            it.detailAddress = domain.address?.detailAddress
            it.schoolName = domain.school?.schoolName
            it.schoolType = domain.school?.schoolType
            it.grade = domain.school?.grade
            it.status = domain.status
        } ?: throw ResourceNotFoundException(ErrorCode.STUDENT_NOT_FOUND)
    }

    fun delete(id: Long) {
        StudentEntity.findById(id)?.delete()
            ?: throw ResourceNotFoundException(ErrorCode.STUDENT_NOT_FOUND)
    }

}