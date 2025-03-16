package me.jimmyberg.ams.v1.relation.repository.entity

import me.jimmyberg.ams.v1.parent.repository.entity.Parents
import me.jimmyberg.ams.v1.student.repository.entity.Students
import org.jetbrains.exposed.sql.Table

object StudentParents : Table("student_parents") {
    
    val studentId = long("student_id").references(Students.id)
    val parentId = long("parent_id").references(Parents.id)

    override val primaryKey = PrimaryKey(studentId, parentId, name = "pk_student_parent")

    init {
        index("idx_student_parent", true, studentId, parentId)
    }

}