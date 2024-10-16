package com.upshift.spring.security

import com.upshift.spring.config.SecurityProperties
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException

class JWTAuthorizationFilter(
    authManager: AuthenticationManager,
    private val tokenProvider: TokenProvider,
) : BasicAuthenticationFilter(authManager) {
    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain,
    ) {
        val header = req.getHeader(SecurityProperties.HEADER_STRING)
        if (header == null || !header.startsWith(SecurityProperties.TOKEN_PREFIX)) {
            chain.doFilter(req, res)
            return
        }
        tokenProvider.getAuthentication(header)?.let { authentication ->
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain.doFilter(req, res)
    }
}
