package me.jimmyberg.ams.v1.parent.repository.table

import org.jetbrains.exposed.dao.id.LongIdTable

object Parents : LongIdTable("parents") {
    val name          = varchar("name", 128)
    val phone         = varchar("phone", 32)
    val birth         = varchar("birth", 8).nullable()
    val gender        = varchar("gender", 6).nullable()
    val zipCode       = varchar("zip_code", 6).nullable()
    val baseAddress   = varchar("base_address", 128).nullable()
    val detailAddress = varchar("detail_address", 128).nullable()
    val status        = varchar("status", 20)

    init {
        index("idx_parent_name_phone", false, name, phone)
    }
}