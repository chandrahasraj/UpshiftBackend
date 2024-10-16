package com.upshift.spring.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "users")
data class UserDO(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long? = null,
    @Column(nullable = false, unique = true)
    var username: String? = null,
    @Column(nullable = false)
    var email: String? = null,
    @Column(nullable = false)
    var password: String? = null,
    @Column(nullable = false)
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], targetEntity = RoleDO::class)
    @JoinTable(
        name = "app_user_role",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
    )
    var roles: Set<RoleDO>? = null,
)
