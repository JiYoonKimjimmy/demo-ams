package me.jimmyberg.ams.domain.model

import me.jimmyberg.ams.common.enumerate.ActivationStatus
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.presentation.dto.StudentDTO

data class Student(
    val id: Long? = null,
    val name: String,
    val nameLabel: Int? = null,
    val phone: String,
    val birth: String,
    val gender: Gender,
    val address: Address? = null,
    val school: School? = null,
    val status: ActivationStatus = ActivationStatus.REGISTER_WAITING
) {

    fun update(dto: StudentDTO): Student {
        return copy(
            name = dto.name ?: name,
            phone = dto.phone ?: phone,
            birth = dto.birth ?: birth,
            gender = dto.gender ?: gender,
            address = dto.address ?: address,
            school = dto.school ?: school,
            status = dto.status ?: status
        )
    }
}