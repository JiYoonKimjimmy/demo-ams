package me.jimmyberg.ams.infrastructure.adapter.outbound.exposed.entity

import me.jimmyberg.ams.common.enumerate.ActivationStatus
import me.jimmyberg.ams.common.enumerate.Gender
import org.jetbrains.exposed.dao.id.LongIdTable

object ParentTable : LongIdTable("parents") {
    val name          = varchar("name", 128)
    val phone         = varchar("phone", 32)
    val birth         = varchar("birth", 8).nullable()
    val gender        = enumerationByName("gender", 6, Gender::class).nullable()
    val zipCode       = varchar("zip_code", 6).nullable()
    val baseAddress   = varchar("base_address", 128).nullable()
    val detailAddress = varchar("detail_address", 128).nullable()
    val status        = enumerationByName("status", 20, ActivationStatus::class)

    init {
        index("idx_parent_name_phone", false, name, phone)
    }
}