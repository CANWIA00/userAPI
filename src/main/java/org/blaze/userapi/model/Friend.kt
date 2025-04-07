package org.blaze.userapi.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "friends")
data class Friend(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "sender_id")
    @JsonIgnore
    val sender: Profile,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "receiver_id")
    @JsonIgnore
    val receiver: Profile,

    @Enumerated(EnumType.STRING)
    val status: F_status,

    val createdAt: LocalDateTime
) : Serializable{
    constructor() : this (
        id = null,
        sender = Profile(),
        receiver = Profile(),
        status = F_status.PENDING,
        createdAt = LocalDateTime.now()
    )



}
