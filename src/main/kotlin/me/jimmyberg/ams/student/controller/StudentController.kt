package me.jimmyberg.ams.student.controller

import me.jimmyberg.ams.student.controller.model.FindAllStudentResponse
import me.jimmyberg.ams.student.controller.model.FindStudentResponse
import me.jimmyberg.ams.student.controller.model.SaveStudentRequest
import me.jimmyberg.ams.student.controller.model.SaveStudentResponse
import me.jimmyberg.ams.student.domain.Student
import me.jimmyberg.ams.student.service.FindStudentService
import me.jimmyberg.ams.student.service.SaveStudentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/student")
@RestController
class StudentController(
    private val saveStudentService: SaveStudentService,
    private val findStudentService: FindStudentService
) {

    @PostMapping
    fun saveStudent(@RequestBody request: SaveStudentRequest): ResponseEntity<SaveStudentResponse> {
        val student = Student(
            name = request.student.name,
            phone = request.student.phone,
            birthday = request.student.birthday,
            gender = request.student.gender,
            address = request.student.address,
            schoolName = request.student.schoolName,
            schoolType = request.student.schoolType,
            grade = request.student.grade,
            status = request.student.status
        )
        val response = SaveStudentResponse(student = saveStudentService.save(student))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun findOne(@PathVariable id: String): ResponseEntity<FindStudentResponse> {
        val response = FindStudentResponse(findStudentService.findOne(id))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/all")
    fun findAll(): ResponseEntity<FindAllStudentResponse> {
        val response = FindAllStudentResponse(findStudentService.findAll())
        return ResponseEntity(response, HttpStatus.OK)
    }

}