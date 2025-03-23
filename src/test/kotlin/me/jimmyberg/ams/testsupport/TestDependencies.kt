package me.jimmyberg.ams.testsupport

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import me.jimmyberg.ams.v1.student.repository.FakeStudentRepositoryImpl
import me.jimmyberg.ams.v1.student.repository.StudentExposedRepository
import me.jimmyberg.ams.v1.student.repository.StudentRepositoryImpl
import me.jimmyberg.ams.v1.student.repository.document.StudentDocumentFixture
import me.jimmyberg.ams.v1.student.service.FindStudentServiceImpl
import me.jimmyberg.ams.v1.student.service.SaveStudentServiceImpl
import me.jimmyberg.ams.v1.student.service.domain.StudentFixture
import me.jimmyberg.ams.v1.student.service.domain.StudentMapper

object TestDependencies {

    // mapper
    val studentMapper = StudentMapper()

    // repository
    val studentExposedRepository = StudentExposedRepository()
    val studentRepository = StudentRepositoryImpl(studentMapper, studentExposedRepository)
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