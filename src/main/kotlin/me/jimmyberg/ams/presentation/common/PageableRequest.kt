package me.jimmyberg.ams.presentation.common

import org.jetbrains.exposed.sql.SortOrder

data class PageableRequest(
    val number: Int = 0,
    val size: Int = 10,
    val fromDate: String? = null,
    val toDate: String? = null,
    var sortBy: String? = null,
    val sortOrder: SortOrder = SortOrder.DESC
) {
    val offset: Long
        get() = number.toLong() * size
}