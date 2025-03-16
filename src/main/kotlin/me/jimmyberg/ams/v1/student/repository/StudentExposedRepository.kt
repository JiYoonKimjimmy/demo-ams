package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.v1.student.repository.entity.StudentEntity
import me.jimmyberg.ams.v1.student.service.domain.Student
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

    fun findById(id: String): StudentEntity? {
        return StudentEntity.findById(id.toLong())
    }

}