package me.jimmyberg.ams.presentation.controller

import me.jimmyberg.ams.application.usecase.StudentManagementUseCase
import me.jimmyberg.ams.domain.model.StudentMapper
import me.jimmyberg.ams.presentation.model.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/student")
@RestController
class StudentManagementController(
    private val studentMapper: StudentMapper,
    private val studentManagementUseCase: StudentManagementUseCase,
) {

    @PostMapping
    fun save(@RequestBody request: SaveStudentRequest): ResponseEntity<SaveStudentResponse> {
        return SaveStudentResponse(student = studentManagementUseCase.saveStudent(model = request.student))
            .success(HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun findOne(@PathVariable id: String): ResponseEntity<FindStudentResponse> {
        return FindStudentResponse(student = studentManagementUseCase.findStudent(id))
            .success(HttpStatus.OK)
    }

    @GetMapping("/scroll")
    fun scroll(@RequestBody request: ScrollStudentsRequest = ScrollStudentsRequest()): ResponseEntity<ScrollStudentsResponse> {
        return studentManagementUseCase.scrollStudents(
                predicate = studentMapper.requestToPredicate(request),
                pageable = request.pageable
            )
            .let { ScrollStudentsResponse(scrollContent = it) }
            .success(HttpStatus.OK)
    }

    @PutMapping
    fun update(@RequestBody request: UpdateStudentRequest): ResponseEntity<UpdateStudentResponse> {
        return UpdateStudentResponse(student = studentManagementUseCase.updateStudent(model = request.student))
            .success(HttpStatus.OK)
    }

}