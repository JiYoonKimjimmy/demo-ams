package me.jimmyberg.ams.domain.model.predicate

import me.jimmyberg.ams.common.enumerate.ActivationStatus
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.model.PageableRequest

data class StudentPredicate(
    val id: Long? = null,
    val name: String? = null,
    val phone: String? = null,
    val birth: String? = null,
    val gender: Gender? = null,
    val school: SchoolPredicate? = null,
    val status: ActivationStatus? = null,
    val pageable: PageableRequest = PageableRequest(),
)