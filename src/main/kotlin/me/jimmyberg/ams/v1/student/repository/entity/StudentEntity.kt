package me.jimmyberg.ams.v1.student.repository.entity

import me.jimmyberg.ams.common.domain.Address
import me.jimmyberg.ams.v1.student.service.domain.Student.School
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class StudentEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<StudentEntity>(StudentTable)

    var name          by StudentTable.name
    var nameLabel     by StudentTable.nameLabel
    var phone         by StudentTable.phone
    var birth         by StudentTable.birth
    var gender        by StudentTable.gender
    var zipCode       by StudentTable.zipCode
    var baseAddress   by StudentTable.baseAddress
    var detailAddress by StudentTable.detailAddress
    var schoolName    by StudentTable.schoolName
    var schoolType    by StudentTable.schoolType
    var grade         by StudentTable.grade
    var status        by StudentTable.status

    val address: Address? by lazy {
        takeIf { zipCode != null && baseAddress != null && detailAddress != null }
            ?.let { Address(zipCode!!, baseAddress!!, detailAddress!!) }
    }

    val school: School? by lazy {
        takeIf { schoolName != null && schoolType != null && grade != null }
            ?.let { School(schoolName!!, schoolType!!, grade!!) }
    }

}