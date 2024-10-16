package com.upshift.spring.service

import com.upshift.spring.entity.RoleDO
import com.upshift.spring.entity.SupportedRole
import com.upshift.spring.mapper.UserDoToUserDoMapper
import com.upshift.spring.mapper.UserDoToUserOutputMapper
import com.upshift.spring.mapper.UserInputToUserDOMapper
import com.upshift.spring.model.UserInput
import com.upshift.spring.model.UserOutput
import com.upshift.spring.repository.UserRepository
import com.upshift.spring.security.TokenProvider
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    @Autowired private val tokenProvider: TokenProvider,
    @Autowired private val bcryptEncoder: BCryptPasswordEncoder,
    @PersistenceContext private val entityManager: EntityManager,
) {
    private val mapper: UserInputToUserDOMapper = Mappers.getMapper(UserInputToUserDOMapper::class.java)
    private val userDoToUserDoMapper: UserDoToUserDoMapper = Mappers.getMapper(UserDoToUserDoMapper::class.java)
    private val userDoToUserOutputMapper: UserDoToUserOutputMapper =
        Mappers.getMapper(UserDoToUserOutputMapper::class.java)

    @Transactional
    fun saveRole(roleDO: RoleDO) {
        entityManager.persist(roleDO)
    }

    @Transactional
    fun save(userInput: UserInput): UserOutput {
        val userDO = mapper.mapUserInputToUserDO(userInput)
        userDO.password = bcryptEncoder.encode(userInput.password)
        val updatedDo = userRepository.save(userDO)
        updatedDo.roles!!.map {
            it.userDO = updatedDo
            saveRole(it)
        }
        return userDoToUserOutputMapper.mapUserDoToUserOutputWithToken(
            updatedDo,
            createToken(updatedDo.username, updatedDo.password, updatedDo.roles),
        )
    }

    fun findByUsername(username: String): UserOutput =
        userRepository
            .findByUsername(username)
            .map {
                userDoToUserOutputMapper.mapUserDoToUserOutputWithoutToken(it)
            }.orElseThrow { UsernameNotFoundException("User not found") }

    @Transactional
    fun update(userInput: UserInput): UserOutput {
        val userDO = mapper.mapUserInputToUserDO(userInput)
        return userRepository
            .findByUsername(userInput.username)
            .map { existingUser ->
                val toUpdateUserDo = userDoToUserDoMapper.mapUserDoToUserDo(userDO, existingUser)
                toUpdateUserDo.roles = userDO.roles.takeIf { !it.isNullOrEmpty() } ?: existingUser.roles
                val updatedDo = userRepository.save(toUpdateUserDo)
                updatedDo.roles!!.map {
                    it.userDO = updatedDo
                    saveRole(it)
                }

                return@map userDoToUserOutputMapper.mapUserDoToUserOutputWithToken(
                    updatedDo,
                    createToken(updatedDo.username, updatedDo.password, updatedDo.roles),
                )
            }.orElseThrow { UsernameNotFoundException("User not found") }
    }

    private fun createToken(
        username: String?,
        password: String?,
        roles: Set<RoleDO>?,
    ): String =
        tokenProvider
            .createToken(
                UsernamePasswordAuthenticationToken(
                    username,
                    password,
                    AuthorityUtils.createAuthorityList(
                        roles?.map { it.accessRole?.roleType }?.toList()
                            ?: listOf(SupportedRole.STANDARD_USER.roleType),
                    ),
                ),
            )
}
