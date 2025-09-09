package me.jimmyberg.ams.common.model

import me.jimmyberg.ams.common.DEFAULT_SORT_ORDER
import org.jetbrains.exposed.sql.SortOrder

data class PageableRequest(
    val number: Int = 0,
    val size: Int = 10,
    val fromDate: String? = null,
    val toDate: String? = null,
    val sortBy: String? = DEFAULT_SORT_ORDER,
    val sortOrder: SortOrder = SortOrder.DESC
) {
    val offset: Long
        get() = number.toLong() * size
}