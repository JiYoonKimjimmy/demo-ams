package me.jimmyberg.ams.student.controller

import me.jimmyberg.ams.student.controller.model.FindAllStudentResponse
import me.jimmyberg.ams.student.service.FindStudentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/student")
@RestController
class FindStudentController(
    private val findStudentService: FindStudentService
) {

    @GetMapping("/all")
    fun findAll(): ResponseEntity<FindAllStudentResponse> {
        val response = FindAllStudentResponse(data = findStudentService.findAll())
        return ResponseEntity<FindAllStudentResponse>(response, HttpStatus.OK)
    }

}