package me.jimmyberg.ams.domain.model.predicate

import me.jimmyberg.ams.common.enumerate.SchoolType

data class SchoolPredicate(
    val schoolName: String? = null,
    val schoolType: SchoolType? = null,
    val grade: Int? = null
)