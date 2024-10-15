package com.upshift.spring.mapper

import com.upshift.spring.entity.RoleDO
import com.upshift.spring.entity.UserDO
import com.upshift.spring.model.RoleBO
import com.upshift.spring.model.UserOutput
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.NullValueCheckStrategy

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
interface UserDoToUserOutputMapper {
    @Mapping(target = "roles", expression = "java(mapRolesDOToRoleBO(userDo.getRoles()))")
    @Mapping(target = "email", source = "userDo.email")
    @Mapping(target = "username", source = "userDo.username")
    @Mapping(target = "id", source = "userDo.id")
    @Mapping(target = "token", expression = "java(token)")
    fun mapUserDoToUserOutput(userDo: UserDO, token: String): UserOutput

    fun mapRolesDOToRoleBO(roles: Set<RoleDO>): Set<RoleBO> {
        return roles.map { mapRoleToRoleDOAccessRole(it) }.toSet()
    }

    @Mapping(target = "roleName", expression = "java(role.getAccessRole().getRoleType())")
    fun mapRoleToRoleDOAccessRole(role: RoleDO): RoleBO
}