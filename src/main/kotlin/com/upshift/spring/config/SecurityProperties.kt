package com.upshift.spring.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties(prefix = "jwt-security")
@Validated
class SecurityProperties {
    var secret = ""

    var expirationTime: Int = 31 // in days

    var strength = 10

    // constant
    val tokenPrefix = "Bearer "
    val headerString = "Authorization"
}