package me.jimmyberg.ams.domain.model

import me.jimmyberg.ams.common.enumerate.SchoolType

data class School(
    val schoolName: String,
    val schoolType: SchoolType,
    val grade: Int,
)