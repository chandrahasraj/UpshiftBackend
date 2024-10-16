package com.upshift.spring.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserOutput(
    var id: Long? = null,
    var username: String? = null,
    var email: String? = null,
    var roles: Set<RoleBO>? = null,
    var token: String? = null,
)
