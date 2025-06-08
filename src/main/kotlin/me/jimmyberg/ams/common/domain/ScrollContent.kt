package me.jimmyberg.ams.common.domain

import com.fasterxml.jackson.annotation.JsonIgnore

data class ScrollContent<R>(
    val size: Int,
    val isEmpty: Boolean,
    val hasNext: Boolean,
    @field:JsonIgnore
    val content: List<R>
) {

    companion object {
        fun <T, R> from(content: List<T>, hasNext: Boolean, mapper: (T) -> R): ScrollContent<R> {
            return ScrollContent(
                size = content.size,
                isEmpty = content.isEmpty(),
                hasNext = hasNext,
                content = content.map(mapper)
            )
        }

        fun <T, R> from(scroll: ScrollContent<T>, mapper: (T) -> R): ScrollContent<R> {
            return ScrollContent(
                size = scroll.size,
                isEmpty = scroll.isEmpty,
                hasNext = scroll.hasNext,
                content = scroll.content.map(mapper)
            )
        }
    }

}