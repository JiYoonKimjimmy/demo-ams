package me.jimmyberg.ams.v1.student.repository.entity

import me.jimmyberg.ams.common.enumerate.ActivationStatus
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import org.jetbrains.exposed.dao.id.LongIdTable

object Students : LongIdTable("students") {
    val name          = varchar("name", 128)
    val nameLabel     = varchar("name_label", 128).nullable()
    val phone         = varchar("phone", 32)
    val birth         = varchar("birth", 8)
    val gender        = enumerationByName("gender", 6, Gender::class)
    val zipCode       = varchar("zip_code", 6).nullable()
    val baseAddress   = varchar("base_address", 128).nullable()
    val detailAddress = varchar("detail_address", 128).nullable()
    val schoolName    = varchar("school_name", 128).nullable()
    val schoolType    = enumerationByName("school_type", 20, SchoolType::class).nullable()
    val grade         = integer("grade").nullable()
    val status        = enumerationByName("status", 20, ActivationStatus::class)

    init {
        index("idx_student_name", true, name, nameLabel)
        index("idx_student_name_phone", false, name, phone)
        index("idx_student_name_school", false, name, schoolName, schoolType, grade)
    }
}