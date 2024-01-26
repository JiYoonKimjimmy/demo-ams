package me.jimmyberg.ams.student.service

import me.jimmyberg.ams.common.domain.Address
import me.jimmyberg.ams.student.controller.model.StudentModelMapper
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UpdateStudentServiceBootTest(
    @Autowired private val findStudentService: FindStudentServiceV1,
    @Autowired private val updateStudentService: UpdateStudentServiceV1,
    @Autowired private val mapper: StudentModelMapper
) {

    @Test
    fun `학생 정보 조회 후 주소 변경하여 업데이트 저장한다`() {
        // given
        val studentId = "65abefa1cf7fcb5a102ccdea"
        val address = Address("12345", "서울시 영등포구 은행로 25", "Hello World")
        val student = findStudentService
            .findOne(studentId)
            .apply {
                this.zipCode = address.zipCode
                this.baseAddress = address.baseAddress
                this.detailAddress = address.detailAddress
            }

        // when
        val result = updateStudentService.update(mapper.modelToDomain(student))

        // then
        assertNotNull(result)
    }
}