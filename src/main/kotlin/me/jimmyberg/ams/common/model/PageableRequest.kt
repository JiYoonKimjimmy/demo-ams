package me.jimmyberg.ams.common.model

import me.jimmyberg.ams.common.DEFAULT_SORT_ORDER
import org.springframework.data.domain.PageRequest

data class PageableRequest(
    val number: Int = 0,
    val size: Int = 10,
    val fromDate: String? = null,
    val toDate: String? = null,
    var sortBy: String? = null,
    val sortOrder: String? = DEFAULT_SORT_ORDER
) {
    fun toPageRequest() = PageRequest.of(number, size)
}