package me.jimmyberg.ams.v1.student.controller

import me.jimmyberg.ams.v1.student.controller.model.*
import me.jimmyberg.ams.v1.student.service.FindStudentServiceV1
import me.jimmyberg.ams.v1.student.service.SaveStudentServiceV1
import me.jimmyberg.ams.v1.student.service.UpdateStudentServiceV1
import me.jimmyberg.ams.v1.student.service.domain.StudentMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/student")
@RestController
class StudentManagementController(
    private val saveStudentService: SaveStudentServiceV1,
    private val findStudentService: FindStudentServiceV1,
    private val updateStudentService: UpdateStudentServiceV1,
    private val mapper: StudentMapper
) {

    @PostMapping
    fun saveStudent(@RequestBody request: SaveStudentRequest): ResponseEntity<SaveStudentResponse> {
        return mapper.modelToDomain(request.student)
            .let { saveStudentService.save(it) }
            .let { mapper.domainToModel(it) }
            .let { SaveStudentResponse(it) }
            .success(HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun findOne(@PathVariable id: String): ResponseEntity<FindStudentResponse> {
        return findStudentService.findOne(id)
            .let { mapper.domainToModel(it) }
            .let { FindStudentResponse(it) }
            .success(HttpStatus.OK)
    }

    @GetMapping("/all")
    fun findAll(): ResponseEntity<FindAllStudentResponse> {
        return findStudentService.findAll()
            .map { mapper.domainToModel(it) }
            .let { FindAllStudentResponse(it) }
            .success(HttpStatus.OK)
    }

    @PutMapping
    fun update(@RequestBody request: UpdateStudentRequest): ResponseEntity<UpdateStudentResponse> {
        return mapper.modelToDomain(request.student)
            .let { updateStudentService.update(it) }
            .let { mapper.domainToModel(it) }
            .let { UpdateStudentResponse(it) }
            .success(HttpStatus.OK)
    }

}