package me.jimmyberg.ams.presentation.controller

import me.jimmyberg.ams.domain.model.StudentMapper
import me.jimmyberg.ams.domain.port.inbound.FindStudentService
import me.jimmyberg.ams.domain.port.inbound.SaveStudentService
import me.jimmyberg.ams.domain.port.inbound.UpdateStudentService
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate
import me.jimmyberg.ams.presentation.model.FindStudentResponse
import me.jimmyberg.ams.presentation.model.SaveStudentRequest
import me.jimmyberg.ams.presentation.model.SaveStudentResponse
import me.jimmyberg.ams.presentation.model.ScrollStudentsRequest
import me.jimmyberg.ams.presentation.model.ScrollStudentsResponse
import me.jimmyberg.ams.presentation.model.UpdateStudentRequest
import me.jimmyberg.ams.presentation.model.UpdateStudentResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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