package com.upshift.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UpShiftBackendApplication

fun main(args: Array<String>) {
    runApplication<UpShiftBackendApplication>(*args)
}
