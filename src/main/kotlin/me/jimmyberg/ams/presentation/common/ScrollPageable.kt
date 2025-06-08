package me.jimmyberg.ams.presentation.common

import me.jimmyberg.ams.domain.common.ScrollResult

data class ScrollPageable(
    val size: Int,
    val hasNext: Boolean,
    val isEmpty: Boolean,
) {
    companion object {
        fun <T> from(scrollResult: ScrollResult<T>): ScrollPageable {
            return ScrollPageable(
                size = scrollResult.size,
                hasNext = scrollResult.hasNext,
                isEmpty = scrollResult.isEmpty,
            )
        }
    }
}