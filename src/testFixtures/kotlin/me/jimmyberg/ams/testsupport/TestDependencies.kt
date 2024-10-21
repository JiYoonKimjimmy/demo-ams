package me.jimmyberg.ams.testsupport

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import me.jimmyberg.ams.v1.student.repository.FakeStudentRepository
import me.jimmyberg.ams.v1.student.repository.document.StudentDocumentFixture
import me.jimmyberg.ams.v1.student.service.FindStudentServiceImpl
import me.jimmyberg.ams.v1.student.service.SaveStudentServiceImpl
import me.jimmyberg.ams.v1.student.service.domain.StudentMapper

object TestDependencies {

    val studentMapper = StudentMapper()

    val fakeStudentRepository = FakeStudentRepository()

    val saveStudentService = SaveStudentServiceImpl(studentMapper, fakeStudentRepository)
    val findStudentService = FindStudentServiceImpl(studentMapper, fakeStudentRepository)

    val studentDocumentFixture = StudentDocumentFixture()

    val objectMapper: ObjectMapper = jacksonObjectMapper().registerModule(kotlinModule())

}