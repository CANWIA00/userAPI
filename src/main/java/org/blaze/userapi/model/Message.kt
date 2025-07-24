package org.blaze.userapi.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity

data class Message (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    val chatRoomId: String?,
    val senderMail: String?,
    val receiverMail: String?,
    val content: String?,
    val type: MessageType?,
    val createdAt: LocalDateTime = LocalDateTime.now(),
): Serializable {
    constructor() : this (
        id = null,
        chatRoomId = null,
        senderMail = null,
        receiverMail = null,
        content = null,
        type = null,
        createdAt = LocalDateTime.now(),
    )
}

