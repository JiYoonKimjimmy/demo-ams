package me.jimmyberg.ams.v1.student.controller

import me.jimmyberg.ams.v1.student.controller.model.*
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import me.jimmyberg.ams.v1.student.service.FindStudentService
import me.jimmyberg.ams.v1.student.service.SaveStudentService
import me.jimmyberg.ams.v1.student.service.UpdateStudentService
import me.jimmyberg.ams.v1.student.service.domain.StudentMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/student")
@RestController
class StudentManagementController(
    private val studentMapper: StudentMapper,
    private val saveStudentService: SaveStudentService,
    private val findStudentService: FindStudentService,
    private val updateStudentService: UpdateStudentService,
) {

    @PostMapping
    fun save(@RequestBody request: SaveStudentRequest): ResponseEntity<SaveStudentResponse> {
        return studentMapper.modelToDomain(request.student)
            .let { saveStudentService.save(it) }
            .let { studentMapper.domainToModel(it) }
            .let { SaveStudentResponse(it) }
            .success(HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun findOne(@PathVariable id: String): ResponseEntity<FindStudentResponse> {
        return StudentPredicate(id = id)
            .let { findStudentService.findOne(it) }
            .let { studentMapper.domainToModel(it) }
            .let { FindStudentResponse(it) }
            .success(HttpStatus.OK)
    }

    @GetMapping("/scroll")
    fun scroll(@RequestBody request: ScrollStudentsRequest = ScrollStudentsRequest()): ResponseEntity<ScrollStudentsResponse> {
        return studentMapper.requestToPredicate(request)
            .let { findStudentService.scroll(it, request.pageable) }
            .let { ScrollStudentsResponse(it, studentMapper::domainToModel) }
            .success(HttpStatus.OK)
    }

    @PutMapping
    fun update(@RequestBody request: UpdateStudentRequest): ResponseEntity<UpdateStudentResponse> {
        return studentMapper.modelToDomain(request.student)
            .let { updateStudentService.update(it) }
            .let { studentMapper.domainToModel(it) }
            .let { UpdateStudentResponse(it) }
            .success(HttpStatus.OK)
    }

}