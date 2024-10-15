package com.upshift.spring.entity

import jakarta.persistence.*

@Entity
@Table(name = "app_role")
class RoleDO(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "access_role", updatable = false)
    var accessRole: SupportedRole? = SupportedRole.STANDARD_USER,

    @ManyToOne(cascade = [CascadeType.MERGE], fetch = FetchType.EAGER, targetEntity = UserDO::class)
    var userDO: UserDO? = null
)