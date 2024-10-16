package com.upshift.spring.exception

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        ex: RuntimeException,
        request: WebRequest,
    ): ResponseEntity<Any?> {
        val apiError = ServiceException(HttpStatus.BAD_REQUEST)
        return handleExceptionInternal(
            ex,
            apiError,
            HttpHeaders(),
            HttpStatus.BAD_REQUEST,
            request,
        )!!
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(
        ex: Exception,
        request: WebRequest,
    ): ResponseEntity<Any> {
        val apiError =
            ServiceException(
                HttpStatus.UNAUTHORIZED,
                "Authentication failed at controller advice",
                ex as Throwable,
            )
        return handleExceptionInternal(
            ex,
            apiError,
            HttpHeaders(),
            HttpStatus.UNAUTHORIZED,
            request,
        )!!
    }
}
