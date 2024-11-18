package me.jimmyberg.ams.testsupport

import me.jimmyberg.ams.common.document.BaseDocument
import me.jimmyberg.ams.common.model.PageableRequest

open class FakeBaseRepository<T : BaseDocument> {

    protected val documents = mutableMapOf<String, T>()

    protected fun save(entity: T): T {
        val id = entity.id ?: TestExtensionFunctions.generateUUID()
        documents[id] = entity
        return entity.apply { this.id = id }
    }

    protected fun findById(id: String): T? {
        return documents[id]
    }

    protected fun findPage(pageable: PageableRequest, predicate: (T) -> Boolean): Pair<Int, List<T>> {
        val total = this.documents.values.filter { predicate(it) }
        val start = pageable.number * pageable.size
        val last = ((pageable.number + 1) * pageable.size) - 1
        val end = if (total.size < last) total.size else last
        val content = total.subList(start, end)
        return Pair(total.size, content)
    }

}