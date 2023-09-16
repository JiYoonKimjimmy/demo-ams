package me.jimmyberg.ams

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoAmsApplication

fun main(args: Array<String>) {
    runApplication<DemoAmsApplication>(*args)
}
