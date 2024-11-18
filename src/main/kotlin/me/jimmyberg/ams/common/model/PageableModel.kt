package me.jimmyberg.ams.common.model

import me.jimmyberg.ams.common.domain.PageableContent

data class PageableModel(
    val size: Int,
    val hasNext: Boolean,
    val isLast: Boolean,
    val isEmpty: Boolean,
) {
    companion object {
        fun <T> from(pageableContent: PageableContent<T>): PageableModel {
            return PageableModel(
                size = pageableContent.size,
                hasNext = pageableContent.hasNext,
                isLast = pageableContent.isLast,
                isEmpty = pageableContent.isEmpty,
            )
        }
    }
}