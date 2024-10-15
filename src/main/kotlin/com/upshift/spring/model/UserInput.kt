package com.upshift.spring.model

data class UserInput(
    var username: String = "",
    var email: String? = null,
    var password: String? = null,
    var roles: Set<RoleBO>? = null
)
