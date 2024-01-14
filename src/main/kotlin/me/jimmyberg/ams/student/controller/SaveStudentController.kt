package me.jimmyberg.ams.student.controller

import me.jimmyberg.ams.student.controller.model.SaveStudentRequest
import me.jimmyberg.ams.student.domain.Student
import me.jimmyberg.ams.student.service.SaveStudentService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/student")
@RestController
class SaveStudentController(
    private val saveStudentService: SaveStudentService
) {

    @PostMapping
    fun saveStudent(@RequestBody request: SaveStudentRequest): Student {
        return Student(
            name = request.name,
            phone = request.phone,
            birthday = request.birthday,
            gender = request.gender,
            address = request.address,
            schoolName = request.schoolName,
            schoolType = request.schoolType,
            grade = request.grade,
            status = request.status
        )
        .let { saveStudentService.save(it) }
    }

}