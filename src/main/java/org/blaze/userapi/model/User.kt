package org.blaze.userapi.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private val id: UUID? = null,

    private val email: String,

    private val password: String,

    private val createTime: LocalDateTime = LocalDateTime.now(),

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private var profile: Profile? = null,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private val userSession: MutableList<UserSession> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private val friends: List<Friend> = mutableListOf(),

    @OneToMany(mappedBy = "friend", fetch = FetchType.LAZY)
    private val friendOf: List<Friend> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private val blockedUsers: List<BlockedUser> = mutableListOf(),

    @OneToMany(mappedBy = "blockedUser", fetch = FetchType.LAZY)
    private val blockedByUsers: List<BlockedUser> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    private val role: Role
) : UserDetails {


    constructor() : this(
        id = null,
        email = "",
        password = "",
        createTime = LocalDateTime.now(),
        profile = null,
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

