package me.jimmyberg.ams.infrastructure.adapter.outbound.exposed.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ParentEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ParentEntity>(ParentTable)

    val name          by ParentTable.name
    val phone         by ParentTable.phone
    val birth         by ParentTable.birth
    val gender        by ParentTable.gender
    val zipCode       by ParentTable.zipCode
    val baseAddress   by ParentTable.baseAddress
    val detailAddress by ParentTable.detailAddress
    val status        by ParentTable.status

    override fun equals(other: Any?): Boolean {
        val otherEntity = other as? ParentEntity ?: return false
        return (this.id == otherEntity.id) ||
        (
            this.name == otherEntity.name
            && this.phone == otherEntity.phone
            && this.birth == otherEntity.birth
            && this.gender == otherEntity.gender
        )
    }

    override fun hashCode(): Int {
        return id.value.hashCode()
    }
}