package org.blaze.userapi.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    val email: String = "",

    // Use a private backing field for password
    @Column(name = "password")
    private val _password: String = "",

    val createTime: LocalDateTime = LocalDateTime.now(),

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var profile: Profile? = null,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    val userSession: MutableList<UserSession> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val blockedUsers: List<BlockedUser> = mutableListOf(),

    @OneToMany(mappedBy = "blockedUser", fetch = FetchType.LAZY)
    val blockedByUsers: List<BlockedUser> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    val role: Role = Role.USER
) : UserDetails {

    constructor() : this(
        id = null,
        email = "",
        _password = "",
        createTime = LocalDateTime.now(),
        profile = null,
        userSession = mutableListOf(),
        blockedUsers = mutableListOf(),
        blockedByUsers = mutableListOf(),
        role = Role.USER
    )

    // Override the getter for password
    override fun getPassword(): String = _password

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(SimpleGrantedAuthority("ROLE_${role.name}"))

    override fun getUsername(): String = email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}

