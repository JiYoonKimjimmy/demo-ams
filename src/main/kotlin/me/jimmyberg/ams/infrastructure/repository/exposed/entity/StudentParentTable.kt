package me.jimmyberg.ams.infrastructure.repository.exposed.entity

import org.jetbrains.exposed.sql.Table

object StudentParentTable : Table("students_parent") {

    val studentId = long("student_id").references(StudentTable.id)
    val parentId = long("parent_id").references(ParentTable.id)

    override val primaryKey = PrimaryKey(studentId, parentId, name = "pk_student_parent")

}