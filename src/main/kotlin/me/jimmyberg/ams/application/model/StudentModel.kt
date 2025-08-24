package me.jimmyberg.ams.application.model

import com.fasterxml.jackson.annotation.JsonIgnore
import me.jimmyberg.ams.domain.model.Address
import me.jimmyberg.ams.domain.model.School
import me.jimmyberg.ams.infrastructure.common.enumerate.ActivationStatus
import me.jimmyberg.ams.infrastructure.common.enumerate.Gender
import me.jimmyberg.ams.infrastructure.common.enumerate.SchoolType

data class StudentModel(
    val id: Long? = null,
    val name: String? = null,
    val phone: String? = null,
    val birth: String? = null,
    val gender: Gender? = null,
    var zipCode: String? = null,
    var baseAddress: String? = null,
    var detailAddress: String? = null,
    val schoolName: String? = null,
    val schoolType: SchoolType? = null,
    val grade: Int? = null,
    val status: ActivationStatus? = null
) {

    @get:JsonIgnore
    val address: Address?
        get() = takeIf { zipCode != null && baseAddress != null && detailAddress != null }
            ?.let { Address(zipCode!!, baseAddress!!, detailAddress!!) }

    @get:JsonIgnore
    val school: School?
        get() = takeIf { schoolName != null && schoolType != null && grade != null }
            ?.let { School(schoolName!!, schoolType!!, grade!!) }

}