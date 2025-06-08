package me.jimmyberg.ams.domain.model

class Address(
    val zipCode: String,
    val baseAddress: String,
    val detailAddress: String
) {
    companion object {
        fun from(zipCode: String?, baseAddress: String?, detailAddress: String?): Address? {
            return if (zipCode != null && baseAddress != null && detailAddress != null) {
                Address(zipCode, baseAddress, detailAddress)
            } else {
                null
            }
        }
    }
}