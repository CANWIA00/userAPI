package org.blaze.userapi.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "friends")
class Friend(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false)
    private val friend: User,

    private val status:F_status,

    private val createTime: LocalDateTime = LocalDateTime.now(),
) {
    constructor() : this(
        id = null,
        user = User(),
        friend = User(),
        status = F_status.PENDING,
        createTime = LocalDateTime.now()
    )
}
