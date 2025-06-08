package me.jimmyberg.ams.common.model

import me.jimmyberg.ams.common.domain.ScrollContent

data class ScrollPageable(
    val size: Int,
    val hasNext: Boolean,
    val isEmpty: Boolean,
) {
    companion object {
        fun <T> from(scrollContent: ScrollContent<T>): ScrollPageable {
            return ScrollPageable(
                size = scrollContent.size,
                hasNext = scrollContent.hasNext,
                isEmpty = scrollContent.isEmpty,
            )
        }
    }
}