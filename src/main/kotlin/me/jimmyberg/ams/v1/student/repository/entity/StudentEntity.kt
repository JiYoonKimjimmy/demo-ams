package me.jimmyberg.ams.v1.student.repository.entity

import me.jimmyberg.ams.common.domain.Address
import me.jimmyberg.ams.v1.student.service.domain.Student.School
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class StudentEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<StudentEntity>(Students)

    var name          by Students.name
    var nameLabel     by Students.nameLabel
    var phone         by Students.phone
    var birth         by Students.birth
    var gender        by Students.gender
    var zipCode       by Students.zipCode
    var baseAddress   by Students.baseAddress
    var detailAddress by Students.detailAddress
    var schoolName    by Students.schoolName
    var schoolType    by Students.schoolType
    var grade         by Students.grade
    var status        by Students.status

    val address: Address? by lazy {
        takeIf { zipCode != null && baseAddress != null && detailAddress != null }
            ?.let { Address(zipCode!!, baseAddress!!, detailAddress!!) }
    }

    val school: School? by lazy {
        takeIf { schoolName != null && schoolType != null && grade != null }
            ?.let { School(schoolName!!, schoolType!!, grade!!) }
    }

}