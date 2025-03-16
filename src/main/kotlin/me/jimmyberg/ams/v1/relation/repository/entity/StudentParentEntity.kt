package me.jimmyberg.ams.v1.relation.repository.entity

import me.jimmyberg.ams.v1.parent.repository.entity.ParentEntity
import me.jimmyberg.ams.v1.student.repository.entity.StudentEntity
import org.jetbrains.exposed.sql.Table

object StudentParentEntity : Table("student_parents") {
    
    val studentId = long("student_id").references(StudentEntity.id)
    val parentId = long("parent_id").references(ParentEntity.id)

    override val primaryKey = PrimaryKey(studentId, parentId, name = "pk_student_parent")

    init {
        index("idx_student_parent", true, studentId, parentId)
    }

}