package com.upshift.spring.security

data class UserLoginDTO(
    val username: String,
    val password: String,
    val verificationCode: String? = null,
)
