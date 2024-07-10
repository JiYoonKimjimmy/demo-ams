package me.jimmyberg.ams.mongodsl.extension

import com.example.kotlinmongo.extension.copy
import me.jimmyberg.ams.mongodsl.clazz.EmptyGroup
import me.jimmyberg.ams.mongodsl.clazz.Field
import me.jimmyberg.ams.mongodsl.clazz.Group
import me.jimmyberg.ams.mongodsl.clazz.Order
import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.BasicQuery
import kotlin.reflect.KProperty1

fun <T, R> BasicQuery.groupBy(
    key: KProperty1<T, R>,
): Group<T, R> {
    return Group(key, this.queryObject.copy())
}

fun BasicQuery.groupBy(): EmptyGroup {
    return EmptyGroup(this.queryObject.copy())
}

fun BasicQuery.where(
    document: Document.() -> Document,
): BasicQuery {
    val queryObject = this.queryObject.copy()
    return BasicQuery(document.invoke(queryObject))
}

fun BasicQuery.sumOf(
    sumField: Document.() -> Field<*, *>,
): Aggregation {
    val document = this.queryObject.copy()
    return EmptyGroup(document).sumOf { sumField.invoke(Document()) }
}

fun BasicQuery.orderBy(
    key: KProperty1<*, *>,
): Order {
    return Order(this, key)
}