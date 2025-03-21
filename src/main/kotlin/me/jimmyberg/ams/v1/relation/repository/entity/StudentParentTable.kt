package me.jimmyberg.ams.v1.relation.repository.entity

import me.jimmyberg.ams.v1.parent.repository.entity.ParentTable
import me.jimmyberg.ams.v1.student.repository.entity.StudentTable
import org.jetbrains.exposed.sql.Table

object StudentParentTable : Table("student_parents") {
    
    val studentId = long("student_id").references(StudentTable.id)
    val parentId = long("parent_id").references(ParentTable.id)

    override val primaryKey = PrimaryKey(studentId, parentId, name = "pk_student_parent")

    init {
        index("idx_student_parent", true, studentId, parentId)
    }

}