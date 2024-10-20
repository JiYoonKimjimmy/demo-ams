package me.jimmyberg.ams.testsupport

import me.jimmyberg.ams.v1.student.repository.FakeStudentRepository
import me.jimmyberg.ams.v1.student.repository.document.StudentDocumentFixture
import me.jimmyberg.ams.v1.student.service.FindStudentService
import me.jimmyberg.ams.v1.student.service.SaveStudentService
import me.jimmyberg.ams.v1.student.service.domain.StudentMapper

object TestDependencies {

    val studentMapper = StudentMapper()

    val fakeStudentRepository = FakeStudentRepository()

    val saveStudentService = SaveStudentService(studentMapper, fakeStudentRepository)
    val findStudentService = FindStudentService(studentMapper, fakeStudentRepository)

    val studentDocumentFixture = StudentDocumentFixture()

}