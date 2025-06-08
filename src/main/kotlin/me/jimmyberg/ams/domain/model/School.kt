package me.jimmyberg.ams.domain.model

import me.jimmyberg.ams.infrastructure.common.enumerate.SchoolType

data class School(
    val schoolName: String,
    val schoolType: SchoolType,
    val grade: Int,
) {
    companion object {
        fun from(schoolName: String?, schoolType: SchoolType?, grade: Int?): School? {
            return if (schoolName != null && schoolType != null && grade != null) {
                School(schoolName, schoolType, grade)
            } else {
                null
            }
        }
    }
}