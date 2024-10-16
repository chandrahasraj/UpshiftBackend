package com.upshift.spring.security

import com.upshift.spring.config.SecurityProperties
import com.upshift.spring.service.AppUserDetailsService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import javax.crypto.SecretKey

@Component
class TokenProvider(
    private val userDetailsService: AppUserDetailsService,
) {
    private var key: SecretKey? = null

    @PostConstruct
    fun init() {
        key = Keys.hmacShaKeyFor(SecurityProperties.SECRET.toByteArray())
    }

    fun createToken(authentication: Authentication): String {
        val tokenValidity = Date.from(Instant.now().plus(SecurityProperties.EXPIRATION_TIME.toLong(), ChronoUnit.HOURS))
        val authClaims: MutableList<String> = mutableListOf()
        authentication.authorities?.let { authorities ->
            authorities.forEach { claim -> authClaims.add(claim.toString()) }
        }

        return Jwts
            .builder()
            .setSubject(authentication.name)
            .claim("auth", authClaims)
            .setExpiration(tokenValidity)
            .signWith(key)
            .compact()
    }

    fun getAuthentication(token: String): Authentication? {
        val claims =
            Jwts
                .parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(CLOCK_SKEW_SECONDS)
                .build()
                .parseClaimsJws(token.replace(SecurityProperties.TOKEN_PREFIX, ""))

        val userDetail = userDetailsService.loadUserByUsername(claims.body.subject)
        val principal = User(userDetail.username, "", userDetail.authorities)
        return UsernamePasswordAuthenticationToken(principal, token, userDetail.authorities)
    }

    companion object {
        private const val CLOCK_SKEW_SECONDS: Long = 3600L
    }
}
