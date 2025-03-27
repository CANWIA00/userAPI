package org.blaze.userapi.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.*
import kotlin.jvm.JvmField

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    val email: String,

    @JvmField
    val password: String,

    val createTime: LocalDateTime = LocalDateTime.now(),

    @OneToOne(mappedBy = "user")
    var profile: Profile? = null,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    val userSession: MutableList<UserSession> = mutableListOf(),



    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val blockedUsers: List<BlockedUser> = mutableListOf(),

    @OneToMany(mappedBy = "blockedUser", fetch = FetchType.LAZY)
    val blockedByUsers: List<BlockedUser> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    val role: Role
) : UserDetails {

    constructor() : this(
        id = null,
        email = "",
        password = "",
        createTime = LocalDateTime.now(),
        profile = null,
        userSession = mutableListOf(),
        blockedUsers = mutableListOf(),
        blockedByUsers = mutableListOf(),
        role = Role.USER
    )

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(SimpleGrantedAuthority("ROLE_${role.name}"))

    override fun getPassword(): String = password

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}

