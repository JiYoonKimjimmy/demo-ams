package me.jimmyberg.ams.infrastructure.repository.exposed

import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.common.error.ErrorCode
import me.jimmyberg.ams.common.error.exception.ResourceNotFoundException
import me.jimmyberg.ams.infrastructure.repository.exposed.entity.StudentEntity
import me.jimmyberg.ams.infrastructure.repository.exposed.entity.StudentQuery
import me.jimmyberg.ams.infrastructure.repository.exposed.entity.StudentTable
import org.jetbrains.exposed.sql.Op
import org.springframework.stereotype.Repository

@Repository
class StudentExposedRepository {

    fun save(student: Student): StudentEntity {
        return StudentEntity.new {
            name = student.name
            nameLabel = student.nameLabel?.toString()
            phone = student.phone
            birth = student.birth
            gender = student.gender
            zipCode = student.address?.zipCode
            baseAddress = student.address?.baseAddress
            detailAddress = student.address?.detailAddress
            schoolName = student.school?.schoolName
            schoolType = student.school?.schoolType
            grade = student.school?.grade
            status = student.status
        }
    }

    fun findById(id: Long): StudentEntity? {
        return StudentEntity.findById(id)
    }

    fun findBy(query: StudentQuery): StudentEntity? {
        return StudentEntity.find(query.conditions()).singleOrNull()
    }

    fun findAllBy(query: StudentQuery): List<StudentEntity> {
        return StudentEntity.find(query.conditions()).toList()
    }

    fun scrollBy(query: StudentQuery): Pair<List<StudentEntity>, Boolean> {
        val pageable = query.pageable
        val sortOrder = when (pageable.sortBy) {
            "id" -> StudentTable.id
            else -> StudentTable.id
        }

        val entities = StudentEntity.find(query.conditions(Op.TRUE))
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
        return StudentEntity.findByIdAndUpdate(domain.id!!) {
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