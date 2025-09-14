package me.jimmyberg.ams.infrastructure.adapter.outbound.exposed.entity

import me.jimmyberg.ams.domain.model.Address
import me.jimmyberg.ams.domain.model.School
import me.jimmyberg.ams.domain.model.Student
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

//    val parent: SizedIterable<ParentEntity> by ParentEntity via ParentTable

    fun toDomain(): Student {
        return Student(
            id = id.value,
            name = name,
            nameLabel = nameLabel?.toInt(),
            phone = phone,
            birth = birth,
            gender = gender,
            address = Address.from(zipCode, baseAddress, detailAddress),
            school = School.from(schoolName, schoolType, grade),
            status = status
        )
    }

    override fun equals(other: Any?): Boolean {
        val otherEntity = other as? StudentEntity ?: return false
        return (this.id == otherEntity.id) ||
        (
            this.name == otherEntity.name
            && this.phone == otherEntity.phone
            && this.birth == otherEntity.birth
            && this.gender == otherEntity.gender
        )
    }

    override fun hashCode(): Int {
        return id.value.hashCode()
    }

}