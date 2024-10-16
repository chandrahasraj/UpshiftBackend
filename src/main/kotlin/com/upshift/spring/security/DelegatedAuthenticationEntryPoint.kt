package com.upshift.spring.security

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver
import java.io.IOException

@Component("delegatedAuthenticationEntryPoint")
class DelegatedAuthenticationEntryPoint : AuthenticationEntryPoint {
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private val resolver: HandlerExceptionResolver? = null

    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException,
    ) {
        resolver!!.resolveException(request!!, response!!, null, authException)
    }
}
