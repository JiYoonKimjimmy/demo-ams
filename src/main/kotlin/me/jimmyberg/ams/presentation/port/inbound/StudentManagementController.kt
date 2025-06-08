package me.jimmyberg.ams.presentation.port.inbound

import me.jimmyberg.ams.application.usecase.StudentManagementUseCase
import me.jimmyberg.ams.domain.model.StudentMapper
import me.jimmyberg.ams.presentation.dto.FindStudentResponse
import me.jimmyberg.ams.presentation.dto.SaveStudentRequest
import me.jimmyberg.ams.presentation.dto.SaveStudentResponse
import me.jimmyberg.ams.presentation.dto.ScrollStudentsRequest
import me.jimmyberg.ams.presentation.dto.ScrollStudentsResponse
import me.jimmyberg.ams.presentation.dto.UpdateStudentRequest
import me.jimmyberg.ams.presentation.dto.UpdateStudentResponse
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
                model = studentMapper.requestToModel(request),
                pageable = request.pageable
            )
            .let { ScrollStudentsResponse(scrollResult = it) }
            .success(HttpStatus.OK)
    }

    @PutMapping
    fun update(@RequestBody request: UpdateStudentRequest): ResponseEntity<UpdateStudentResponse> {
        return UpdateStudentResponse(student = studentManagementUseCase.updateStudent(model = request.student))
            .success(HttpStatus.OK)
    }

}