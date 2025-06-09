package me.jimmyberg.ams.domain.model

import me.jimmyberg.ams.infrastructure.common.enumerate.ActivationStatus
import me.jimmyberg.ams.infrastructure.common.enumerate.Gender
import me.jimmyberg.ams.application.usecase.model.StudentModel
import me.jimmyberg.ams.infrastructure.error.ErrorCode
import me.jimmyberg.ams.infrastructure.error.exception.InvalidRequestException

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

    companion object {
        fun create(model: StudentModel): Student {
            requireNotNull(model.name) { throw InvalidRequestException(ErrorCode.REQUIRED_NAME) }
            requireNotNull(model.phone) { throw InvalidRequestException(ErrorCode.REQUIRED_PHONE) }
            requireNotNull(model.birth) { throw InvalidRequestException(ErrorCode.REQUIRED_BIRTH) }
            requireNotNull(model.gender) { throw InvalidRequestException(ErrorCode.REQUIRED_GENDER) }

            return Student(
                id = model.id?.toLong(),
                name = model.name,
                phone = model.phone,
                birth = model.birth,
                gender = model.gender,
                address = model.address,
                school = model.school,
                status = model.status ?: ActivationStatus.REGISTER_WAITING
            )
        }
    }

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