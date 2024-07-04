package me.jimmyberg.ams.kotest

import io.kotest.core.spec.style.BehaviorSpec
import me.jimmyberg.ams.v1.student.repository.fixture.StudentRepositoryFixture
import me.jimmyberg.ams.v1.student.service.SaveStudentService
import me.jimmyberg.ams.v1.student.service.domain.StudentMapper

abstract class CustomBehaviorSpec(
    body: BaseBehaviorSpec.() -> Unit = {}
) : BaseBehaviorSpec() {

    init {
        body()
    }

}

abstract class BaseBehaviorSpec : BehaviorSpec() {

    val studentRepository = StudentRepositoryFixture()
    val studentMapper = StudentMapper()
    val saveStudentService = SaveStudentService(studentRepository, studentMapper)

}