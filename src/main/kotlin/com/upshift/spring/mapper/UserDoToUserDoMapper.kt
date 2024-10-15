package com.upshift.spring.mapper

import com.upshift.spring.entity.UserDO
import org.mapstruct.*

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
interface UserDoToUserDoMapper {
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "email", source = "userDo.email", defaultExpression = "java(existingUserDo.getEmail())")
    @Mapping(target = "password", source = "userDo.password", defaultExpression = "java(existingUserDo.getPassword())")
    @Mapping(target = "username", source = "userDo.username", defaultExpression = "java(existingUserDo.getUsername())")
    @Mapping(target = "id", source = "userDo.id", defaultExpression = "java(existingUserDo.getId())")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun mapUserDoToUserDo(userDo: UserDO, existingUserDo: UserDO): UserDO
}