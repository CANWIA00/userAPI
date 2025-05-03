package org.blaze.userapi.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "user_sessions")
class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: User

    var device: String = ""

    var ipAddress: String = ""

    var createTime: LocalDateTime = LocalDateTime.now()

    constructor()

    constructor(
        user: User,
        device: String,
        ipAddress: String,
        createTime: LocalDateTime = LocalDateTime.now()
    ) {
        this.user = user
        this.device = device
        this.ipAddress = ipAddress
        this.createTime = createTime
    }
}