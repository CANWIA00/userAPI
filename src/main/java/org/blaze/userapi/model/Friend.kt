package org.blaze.userapi.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "friends")
data class Friend(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    val sender: Profile,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id")
    val receiver: Profile,

    @Enumerated(EnumType.STRING)
    val status: F_status,

    val createdAt: LocalDateTime
) {
    constructor() : this (
        id = null,
        sender = Profile(),
        receiver = Profile(),
        status = F_status.PENDING,
        createdAt = LocalDateTime.now()
    )



}
