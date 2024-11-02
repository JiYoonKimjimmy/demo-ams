package me.jimmyberg.ams.common

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

    private val studentMapper = StudentMapper()

    val fakeStudentRepository = FakeStudentRepositoryImpl(studentMapper)

    val saveStudentService = SaveStudentServiceImpl(fakeStudentRepository)
    val findStudentService = FindStudentServiceImpl(fakeStudentRepository)

    val studentFixture = StudentFixture()
    val studentDocumentFixture = StudentDocumentFixture()

    val objectMapper: ObjectMapper = jacksonObjectMapper().registerModule(kotlinModule())

}