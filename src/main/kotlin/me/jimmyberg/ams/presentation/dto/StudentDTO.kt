package me.jimmyberg.ams.presentation.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import me.jimmyberg.ams.common.enumerate.ActivationStatus
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.domain.model.Address
import me.jimmyberg.ams.domain.model.School

data class StudentDTO(
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