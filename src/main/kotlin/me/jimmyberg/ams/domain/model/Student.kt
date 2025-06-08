package me.jimmyberg.ams.domain.model

import me.jimmyberg.ams.infrastructure.common.enumerate.ActivationStatus
import me.jimmyberg.ams.infrastructure.common.enumerate.Gender
import me.jimmyberg.ams.application.usecase.model.StudentModel

data class Student(
    val id: Long? = null,
    val name: String,
    var nameLabel: Int? = null,
    val phone: String,
    val birth: String,
    val gender: Gender,
    val address: Address? = null,
    val school: School? = null,
    val status: ActivationStatus = ActivationStatus.REGISTER_WAITING
) {
    fun update(model: StudentModel): Student {
        return copy(
            name = model.name ?: name,
            phone = model.phone ?: phone,
            birth = model.birth ?: birth,
            gender = model.gender ?: gender,
            address = model.address ?: address,
            school = model.school ?: school,
            status = model.status ?: status
        )
    }
}