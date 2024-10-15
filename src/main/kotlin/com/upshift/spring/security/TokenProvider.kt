package com.upshift.spring.security

import com.upshift.spring.config.SecurityProperties
import com.upshift.spring.service.AppUserDetailsService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.SecretKey

@Component
class TokenProvider(
    private val securityProperties: SecurityProperties,
    private val userDetailsService: AppUserDetailsService,
) {
    private var key: SecretKey? = null
    val logger: Logger = LoggerFactory.getLogger(TokenProvider::class.java)


    @PostConstruct
    fun init() {
        key = Keys.hmacShaKeyFor(securityProperties.secret.toByteArray())
    }

    fun createToken(authentication: Authentication): String {
        val tokenValidity = Date.from(Instant.now().plus(securityProperties.expirationTime.toLong(), ChronoUnit.HOURS))
        val authClaims: MutableList<String> = mutableListOf()
        authentication.authorities?.let { authorities ->
            authorities.forEach { claim -> authClaims.add(claim.toString()) }
        }

        return Jwts.builder()
            .setSubject(authentication.name)
            .claim("auth", authClaims)
            .setExpiration(tokenValidity)
            .signWith(key)
            .compact()
    }

    fun getAuthentication(token: String): Authentication? {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(3600)
                .build()
                .parseClaimsJws(token.replace(securityProperties.tokenPrefix, ""))

            val userDetail = userDetailsService.loadUserByUsername(claims.body.subject)
            val principal = User(userDetail.username, "", userDetail.authorities)
            UsernamePasswordAuthenticationToken(principal, token, userDetail.authorities)
        } catch (e: Exception) {
            logger.error("Error occurred while parsing token", e)
            return null
        }
    }
}