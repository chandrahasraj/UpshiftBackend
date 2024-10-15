package com.upshift.spring.repository

import com.upshift.spring.entity.UserDO
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface UserRepository : CrudRepository<UserDO, Int> {

    fun save(userDO: UserDO): UserDO
    fun findByEmail(email: String): Optional<UserDO>
    fun findByUsername(username: String): Optional<UserDO>

    @Modifying
    @Query("update RoleDO r set r.userDO = :userId where r.id in (:roleIds)")
    fun updateUserRoles(@Param(value = "userId") userId: Long, @Param(value = "roleIds") roleIds: Set<Long>): Long
}