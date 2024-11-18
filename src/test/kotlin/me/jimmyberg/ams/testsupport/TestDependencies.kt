package me.jimmyberg.ams.testsupport

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import me.jimmyberg.ams.v1.student.repository.FakeStudentRepositoryImpl
import me.jimmyberg.ams.v1.student.repository.document.StudentDocumentFixture
import me.jimmyberg.ams.v1.student.service.FindStudentServiceImpl
import me.jimmyberg.ams.v1.student.service.SaveStudentServiceImpl
import me.jimmyberg.ams.v1.student.service.domain.StudentFixture
import me.jimmyberg.ams.v1.student.service.domain.StudentMapper

object TestDependencies {

    // mapper
    private val studentMapper = StudentMapper()

    // repository
    val fakeStudentRepository = FakeStudentRepositoryImpl(studentMapper)

    // service
    val saveStudentService = SaveStudentServiceImpl(fakeStudentRepository)
    val findStudentService = FindStudentServiceImpl(fakeStudentRepository)

    // fixture
    val studentFixture = StudentFixture()
    val studentDocumentFixture = StudentDocumentFixture()

    // etc
    val objectMapper: ObjectMapper = jacksonObjectMapper().registerModule(kotlinModule())

}