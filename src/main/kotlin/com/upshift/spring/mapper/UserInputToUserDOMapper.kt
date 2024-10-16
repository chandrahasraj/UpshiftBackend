package com.upshift.spring.mapper

import com.upshift.spring.entity.RoleDO
import com.upshift.spring.entity.UserDO
import com.upshift.spring.model.RoleBO
import com.upshift.spring.model.UserInput
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.NullValueCheckStrategy
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
interface UserInputToUserDOMapper {
    @Mapping(target = "roles", expression = "java(mapRolesToRoleDO(userInput.getRoles()))")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "username", source = "username")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun mapUserInputToUserDO(userInput: UserInput): UserDO

    fun mapRolesToRoleDO(roles: Set<RoleBO>): Set<RoleDO> = roles.map { mapRoleToRoleDOAccessRole(it) }.toSet()

    @Mapping(
        target = "accessRole",
        expression = "java(com.upshift.spring.entity.SupportedRole.valueOf(role.getRoleName()))",
    )
    fun mapRoleToRoleDOAccessRole(role: RoleBO): RoleDO
}
