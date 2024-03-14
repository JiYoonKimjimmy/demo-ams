package me.jimmyberg.ams.student.controller

import me.jimmyberg.ams.student.controller.model.*
import me.jimmyberg.ams.student.domain.StudentMapper
import me.jimmyberg.ams.student.service.FindStudentServiceV1
import me.jimmyberg.ams.student.service.SaveStudentServiceV1
import me.jimmyberg.ams.student.service.UpdateStudentServiceV1
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/student")
@RestController
class StudentController(
    private val saveStudentService: SaveStudentServiceV1,
    private val findStudentService: FindStudentServiceV1,
    private val updateStudentService: UpdateStudentServiceV1,
    private val mapper: StudentMapper
) {

    @PostMapping
    fun saveStudent(@RequestBody request: SaveStudentRequest): ResponseEntity<SaveStudentResponse> {
        val student = mapper.modelToDomain(request.student)
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

    @PutMapping
    fun update(@RequestBody request: UpdateStudentRequest): ResponseEntity<UpdateStudentResponse> {
        val student = mapper.modelToDomain(request.student)
        val response = UpdateStudentResponse(updateStudentService.update(student))
        return ResponseEntity(response, HttpStatus.OK)
    }

}