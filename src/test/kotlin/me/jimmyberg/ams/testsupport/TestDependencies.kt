package me.jimmyberg.ams.testsupport

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import me.jimmyberg.ams.domain.model.StudentFixture
import me.jimmyberg.ams.domain.service.StudentFindServiceImpl
import me.jimmyberg.ams.domain.service.StudentSaveServiceImpl
import me.jimmyberg.ams.infrastructure.repository.FakeStudentRepositoryImpl
import me.jimmyberg.ams.infrastructure.repository.StudentRepositoryImpl
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentExposedRepository

object TestDependencies {

    // repository
    val studentExposedRepository = StudentExposedRepository()
    val studentRepository = StudentRepositoryImpl(studentExposedRepository)
    val fakeStudentRepository = FakeStudentRepositoryImpl()

    // service
    val studentSaveService = StudentSaveServiceImpl(fakeStudentRepository)
    val studentFindService = StudentFindServiceImpl(fakeStudentRepository)

    // fixture
    val studentFixture = StudentFixture()

    // etc
    val objectMapper: ObjectMapper = jacksonObjectMapper().registerModule(kotlinModule())

}