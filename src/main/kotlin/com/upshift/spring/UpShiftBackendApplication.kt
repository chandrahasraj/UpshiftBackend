package com.upshift.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UpShiftBackendApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<UpShiftBackendApplication>(*args)
}
