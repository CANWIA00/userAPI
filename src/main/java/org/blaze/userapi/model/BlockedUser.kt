package org.blaze.userapi.model

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "blocked_users")
class BlockedUser : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: User

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_user_id", nullable = false)
    lateinit var blockedUser: User

    var blockedAt: LocalDateTime = LocalDateTime.now()

    constructor()

    constructor(
        user: User,
        blockedUser: User,
        blockedAt: LocalDateTime = LocalDateTime.now()
    ) {
        this.user = user
        this.blockedUser = blockedUser
        this.blockedAt = blockedAt
    }
}

