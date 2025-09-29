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
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.springframework.stereotype.Repository

@Repository
class StudentExposedRepository {

    fun save(student: Student): Student {
        val insertedId = StudentTable.insert { it.mapStudent(student) }[StudentTable.id].value
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

    fun existsByNameAndPhoneAndBirth(name: String, phone: String, birth: String): Boolean {
        return StudentTable
            .selectAll()
            .andWhere { (StudentTable.name eq name) and (StudentTable.phone eq phone) and (StudentTable.birth eq birth) }
            .limit(1)
            .empty()
            .not()
    }

    fun existsByNameAndPhoneAndBirthExceptId(name: String, phone: String, birth: String, excludeId: Long): Boolean {
        return StudentTable
            .selectAll()
            .andWhere { (StudentTable.name eq name) and (StudentTable.phone eq phone) and (StudentTable.birth eq birth) and (StudentTable.id neq excludeId) }
            .limit(1)
            .empty()
            .not()
    }

    fun findMaxNameLabelByName(name: String): Int? {
        return StudentTable
            .selectAll()
            .andWhere { StudentTable.name eq name }
            .mapNotNull { it[StudentTable.nameLabel]?.toIntOrNull() ?: 1 }
            .maxOrNull()
    }

    fun update(student: Student): Student {
        val studentId = student.id!!
        StudentTable.update({ StudentTable.id eq studentId }) { it.mapStudent(student) }
        return findById(studentId)!!
    }

    private fun UpdateBuilder<*>.mapStudent(student: Student) {
        this[StudentTable.name] = student.name
        this[StudentTable.nameLabel] = student.nameLabel?.toString()
        this[StudentTable.phone] = student.phone
        this[StudentTable.birth] = student.birth
        this[StudentTable.gender] = student.gender
        this[StudentTable.zipCode] = student.address?.zipCode
        this[StudentTable.baseAddress] = student.address?.baseAddress
        this[StudentTable.detailAddress] = student.address?.detailAddress
        this[StudentTable.schoolName] = student.school?.schoolName
        this[StudentTable.schoolType] = student.school?.schoolType
        this[StudentTable.grade] = student.school?.grade
        this[StudentTable.status] = student.status
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