package me.jimmyberg.ams.domain.model

import me.jimmyberg.ams.application.usecase.model.StudentModel
import me.jimmyberg.ams.infrastructure.common.enumerate.ActivationStatus
import me.jimmyberg.ams.infrastructure.common.enumerate.Gender
import me.jimmyberg.ams.infrastructure.error.ErrorCode
import me.jimmyberg.ams.infrastructure.error.exception.InvalidRequestException

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

    companion object {
        fun create(model: StudentModel): Student {
            requireNotNull(model.name) { throw InvalidRequestException(ErrorCode.MISSING_REQUIRED_DATA, detailMessage = "Student 'name' is required") }
            requireNotNull(model.phone) { throw InvalidRequestException(ErrorCode.MISSING_REQUIRED_DATA, detailMessage = "Student 'phone' is required") }
            requireNotNull(model.birth) { throw InvalidRequestException(ErrorCode.MISSING_REQUIRED_DATA, detailMessage = "Student 'birth' is required") }
            requireNotNull(model.gender) { throw InvalidRequestException(ErrorCode.MISSING_REQUIRED_DATA, detailMessage = "Student 'gender' is required") }

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