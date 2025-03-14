package me.jimmyberg.ams.v1.student.repository.table

import org.jetbrains.exposed.sql.Table

object Students : Table("students") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val indexOfName = integer("indexOfName")
    val phone = varchar("phone", 32)
    val birth = varchar("birth", 8)
    val gender = varchar("gender", 5)
    val zipCode = varchar("zipCode", 6)
    val baseAddress = varchar("baseAddress", 128)
    val detailAddress = varchar("detailAddress", 128)
    val schoolName = varchar("schoolName", 128)
    val schoolType = varchar("schoolType", 20)
    val grade = integer("grade")
    val status = varchar("status", 20)
}