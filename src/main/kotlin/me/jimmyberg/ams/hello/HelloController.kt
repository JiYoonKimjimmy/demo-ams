package me.jimmyberg.ams.hello

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @GetMapping("/api/hello")
    fun hello(): String {
        return "Hello"
    }

    @PostMapping("/api/hello")
    fun hello(@RequestBody request: Hello): String {
        return "Hello ${request.text}"
    }

    @PostMapping("/api/hello/json")
    fun helloJson(@RequestBody request: Hello): Hello {
        return Hello(text = "Hello ${request.text} !")
    }

}