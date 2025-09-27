package me.jimmyberg.ams.infrastructure.adapter.outbound.exposed

import me.jimmyberg.ams.common.error.ErrorCode
import me.jimmyberg.ams.common.error.exception.ResourceNotFoundException
import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.infrastructure.adapter.outbound.exposed.entity.StudentQuery
import me.jimmyberg.ams.infrastructure.adapter.outbound.exposed.entity.StudentTable
import me.jimmyberg.ams.domain.model.Address
import me.jimmyberg.ams.domain.model.School
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.Op
import org.springframework.stereotype.Repository

@Repository
class StudentExposedRepository {

    fun save(student: Student): Student {
        val insertedId = StudentTable.insert {
            it[name] = student.name
            it[nameLabel] = student.nameLabel?.toString()
            it[phone] = student.phone
            it[birth] = student.birth
            it[gender] = student.gender
            it[zipCode] = student.address?.zipCode
            it[baseAddress] = student.address?.baseAddress
            it[detailAddress] = student.address?.detailAddress
            it[schoolName] = student.school?.schoolName
            it[schoolType] = student.school?.schoolType
            it[grade] = student.school?.grade
            it[status] = student.status
        }[StudentTable.id].value

        return findById(insertedId)!!
    }

    fun findById(id: Long): Student? {
        return StudentTable
            .selectAll()
            .andWhere { StudentTable.id eq id }
            .singleOrNull()
            ?.toDomain()
    }

    fun findBy(query: StudentQuery): Student? {
        return StudentTable
            .selectAll()
            .andWhere { query.conditions() }
            .singleOrNull()
            ?.toDomain()
    }

    fun findAllBy(query: StudentQuery): List<Student> {
        return StudentTable
            .selectAll()
            .andWhere { query.conditions() }
            .map { it.toDomain() }
    }

    fun scrollBy(query: StudentQuery): Pair<List<Student>, Boolean> {
        val pageable = query.pageable
        val sortOrder = when (pageable.sortBy) {
            "id" -> StudentTable.id
            else -> StudentTable.id
        }

        val rows = StudentTable
            .selectAll()
            .andWhere { query.conditions(Op.TRUE) }
            .orderBy(sortOrder to pageable.sortOrder)
            .limit(pageable.size + 1)
            .offset(pageable.offset)
            .toList()

        val hasNext = rows.size > pageable.size
        val result = if (hasNext) rows.dropLast(1) else rows

        return Pair(result.map { it.toDomain() }, hasNext)
    }

    fun findAllByNameAndPhoneAndBirth(name: String, phone: String, birth: String): List<Student> {
        return StudentTable
            .selectAll()
            .andWhere { (StudentTable.name eq name) and (StudentTable.phone eq phone) and (StudentTable.birth eq birth) }
            .map { it.toDomain() }
    }

    fun update(domain: Student): Student {
        val id = domain.id ?: throw ResourceNotFoundException(ErrorCode.STUDENT_NOT_FOUND)
        val updated = StudentTable.update({ StudentTable.id eq id }) {
            it[name] = domain.name
            it[nameLabel] = domain.nameLabel?.toString()
            it[phone] = domain.phone
            it[birth] = domain.birth
            it[gender] = domain.gender
            it[zipCode] = domain.address?.zipCode
            it[baseAddress] = domain.address?.baseAddress
            it[detailAddress] = domain.address?.detailAddress
            it[schoolName] = domain.school?.schoolName
            it[schoolType] = domain.school?.schoolType
            it[grade] = domain.school?.grade
            it[status] = domain.status
        }

        if (updated == 0) throw ResourceNotFoundException(ErrorCode.STUDENT_NOT_FOUND)
        return findById(id) ?: throw ResourceNotFoundException(ErrorCode.STUDENT_NOT_FOUND)
    }

    fun delete(id: Long) {
        val deleted = StudentTable.deleteWhere { StudentTable.id eq id }
        if (deleted == 0) throw ResourceNotFoundException(ErrorCode.STUDENT_NOT_FOUND)
    }

    private fun ResultRow.toDomain(): Student {
        return Student(
            id = this[StudentTable.id].value,
            name = this[StudentTable.name],
            nameLabel = this[StudentTable.nameLabel]?.toIntOrNull(),
            phone = this[StudentTable.phone],
            birth = this[StudentTable.birth],
            gender = this[StudentTable.gender],
            address = Address.from(
                this[StudentTable.zipCode],
                this[StudentTable.baseAddress],
                this[StudentTable.detailAddress]
            ),
            school = School.from(
                this[StudentTable.schoolName],
                this[StudentTable.schoolType],
                this[StudentTable.grade]
            ),
            status = this[StudentTable.status]
        )
    }
}