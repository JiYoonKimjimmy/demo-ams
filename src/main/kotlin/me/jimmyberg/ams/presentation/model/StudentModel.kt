package me.jimmyberg.ams.presentation.model

import com.fasterxml.jackson.annotation.JsonIgnore
import me.jimmyberg.ams.domain.model.Address
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.ActivationStatus
import me.jimmyberg.ams.domain.model.School

data class StudentModel(
    val id: String? = null,
    val name: String,
    val phone: String,
    val birth: String,
    val gender: Gender,
    var zipCode: String? = null,
    var baseAddress: String? = null,
    var detailAddress: String? = null,
    val schoolName: String? = null,
    val schoolType: SchoolType? = null,
    val grade: Int? = null,
    val status: ActivationStatus = ActivationStatus.REGISTER_WAITING
) {

    @get:JsonIgnore
    val address: Address? by lazy {
        takeIf { zipCode != null && baseAddress != null && detailAddress != null }
            ?.let { Address(zipCode!!, baseAddress!!, detailAddress!!) }
    }

    @get:JsonIgnore
    val school: School? by lazy {
        takeIf { schoolName != null && schoolType != null && grade != null }
            ?.let { School(schoolName!!, schoolType!!, grade!!) }
    }

}