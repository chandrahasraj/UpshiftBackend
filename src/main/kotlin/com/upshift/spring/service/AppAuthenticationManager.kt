package com.upshift.spring.service

import com.upshift.spring.entity.UserDO
import com.upshift.spring.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class AppAuthenticationManager(
    private val userRepository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
) : AuthenticationManager {
    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        val password = authentication.credentials.toString()
        val userDO: UserDO =
            userRepository.findByUsername(authentication.name).orElseThrow {
                UsernameNotFoundException("The username ${authentication.name} doesn't exist")
            }
        if (!bCryptPasswordEncoder.matches(password, userDO.password)) {
            throw BadCredentialsException("Bad credentials")
        }

        val authorities = ArrayList<GrantedAuthority>()
        if (userDO.roles != null) {
            userDO.roles!!.forEach { authorities.add(SimpleGrantedAuthority(it.accessRole?.roleType)) }
        }
        return UsernamePasswordAuthenticationToken(userDO.username, userDO.password, authorities)
    }
}
