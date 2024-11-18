package me.jimmyberg.ams.common.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.domain.Window

data class PageableContent<T>(
    val size: Int,
    val hasNext: Boolean,
    val isLast: Boolean,
    val isEmpty: Boolean,
    @field:JsonIgnore
    val content: List<T>
) {

    companion object {
        fun <T, R> from(window: Window<T>, mapper: (T) -> R): PageableContent<R> {
            return PageableContent(
                size = window.size(),
                hasNext = window.hasNext(),
                isEmpty = window.isEmpty,
                isLast = window.isLast,
                content = window.content.map(mapper)
            )
        }
    }
}