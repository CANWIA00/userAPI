package org.blaze.userapi.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class UserSession (

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", nullable = false)
    private val user: User,

    private val device: String,

    private val ipAddress: String,

    private val createTime: LocalDateTime = LocalDateTime.now()
    )