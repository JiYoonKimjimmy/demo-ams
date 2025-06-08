package me.jimmyberg.ams.presentation.port.inbound

import me.jimmyberg.ams.application.usecase.StudentManagementUseCase
import me.jimmyberg.ams.domain.model.StudentMapper
import me.jimmyberg.ams.presentation.dto.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class StudentManagementController(
    private val studentMapper: StudentMapper,
    private val studentManagementUseCase: StudentManagementUseCase,
) {

    @PostMapping("/api/v1/student")
    fun create(@RequestBody request: CreateStudentRequest): ResponseEntity<CreateStudentResponse> {
        return CreateStudentResponse(student = studentManagementUseCase.createStudent(model = request.student))
            .success(HttpStatus.CREATED)
    }

    @GetMapping("/api/v1/student/{id}")
    fun findOne(@PathVariable id: String): ResponseEntity<FindStudentResponse> {
        return FindStudentResponse(student = studentManagementUseCase.findStudent(id))
            .success(HttpStatus.OK)
    }

    @GetMapping("/api/v1/students/scroll")
    fun scroll(@RequestBody request: ScrollStudentsRequest = ScrollStudentsRequest()): ResponseEntity<ScrollStudentsResponse> {
        return studentManagementUseCase.scrollStudents(
                model = studentMapper.requestToModel(request),
                pageable = request.pageable
            )
            .let { ScrollStudentsResponse(scrollResult = it) }
            .success(HttpStatus.OK)
    }

    @PutMapping("/api/v1/student")
    fun update(@RequestBody request: UpdateStudentRequest): ResponseEntity<UpdateStudentResponse> {
        return UpdateStudentResponse(student = studentManagementUseCase.updateStudent(model = request.student))
            .success(HttpStatus.OK)
    }

}