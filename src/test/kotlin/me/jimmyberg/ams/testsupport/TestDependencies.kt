package me.jimmyberg.ams.testsupport

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import me.jimmyberg.ams.v1.student.repository.FakeStudentRepositoryImpl
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentExposedRepository
import me.jimmyberg.ams.infrastructure.repository.StudentRepositoryImpl
import me.jimmyberg.ams.domain.service.FindStudentServiceImpl
import me.jimmyberg.ams.domain.service.SaveStudentServiceImpl
import me.jimmyberg.ams.v1.student.service.domain.StudentFixture
import me.jimmyberg.ams.domain.model.StudentMapper

object TestDependencies {

    // mapper
    val studentMapper = StudentMapper()

    // repository
    val studentExposedRepository = StudentExposedRepository()
    val studentRepository = StudentRepositoryImpl(studentMapper, studentExposedRepository)
    val fakeStudentRepository = FakeStudentRepositoryImpl()

    // service
    val saveStudentService = SaveStudentServiceImpl(fakeStudentRepository)
    val findStudentService = FindStudentServiceImpl(fakeStudentRepository)

    // fixture
    val studentFixture = StudentFixture()

    // etc
    val objectMapper: ObjectMapper = jacksonObjectMapper().registerModule(kotlinModule())

}