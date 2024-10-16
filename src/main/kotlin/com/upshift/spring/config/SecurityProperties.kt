package com.upshift.spring.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties(prefix = "jwt-security")
@Validated
object SecurityProperties {
    const val SECRET = ""
    const val EXPIRATION_TIME: Int = 31 // in days
    const val STRENGTH = 10

    // constant
    const val TOKEN_PREFIX = "Bearer "
    const val HEADER_STRING = "Authorization"
}
