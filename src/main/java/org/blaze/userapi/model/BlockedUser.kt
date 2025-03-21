package org.blaze.userapi.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class BlockedUser(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_user_id", nullable = false)
    val blockedUser: User,

    val blockedAt: LocalDateTime = LocalDateTime.now(),
) {

}
