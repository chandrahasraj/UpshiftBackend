package com.upshift.spring.exception

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ServiceException(
    var status: HttpStatus? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private var timestamp: LocalDateTime? = null,
    var message: String? = null,
    var debugMessage: String? = null,
    private var subErrors: List<AppException> = listOf(),
) {
    constructor(status: HttpStatus) : this(status, LocalDateTime.now(), null, null, listOf())

    constructor(status: HttpStatus, message: String, ex: Throwable) : this(
        status,
        LocalDateTime.now(),
        message,
        ex.localizedMessage,
        listOf(),
    )
}
